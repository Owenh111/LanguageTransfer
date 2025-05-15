package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Speaking {

    @FXML private Label heading;
    @FXML private MediaView mediaView;
    @FXML private AnchorPane anchorPane;
    @FXML private Label instructionLabel1, instructionLabel2, instructionLabel3, instructionLabel4;

    @FXML private Button continueButton, playPromptButton, recordButton, stopButton, playRecordingButton, nextAudioButton;

    private TargetDataLine microphone; // technically an interface to read data from the microphone
    private ByteArrayOutputStream outputStream; // raw microphone input
    private boolean isRecording = false; // used to start and stop isRecording
    private boolean tutorialEnabled = false;
    private boolean lastExercise = false;
    private boolean replay = false;
    private int timesContinuePressed = 0;
    private MediaPlayer player;

    private List<Speaking.AudioItem> audioItems = new ArrayList<>();
    private File recording = new File("recording.wav"); // file defined, originally empty, where we put the audio
    // importantly, the file is already provided here and is written to, so we need 3 files

    Learner learner = Session.getLearner();

    public void initialize() {
        resetUI();
        checkTutorialNeeded(learner.getProgress());
        setListeners();
        decideExerciseContent();
        Session.startColorCycle(anchorPane);
    }
    private static class AudioItem {
        URL mediaURL;
        boolean bonusPhrase;

        AudioItem(URL mediaURL, boolean bonusPhrase) {
            this.mediaURL = mediaURL;
            this.bonusPhrase = bonusPhrase;
        }
    }

    private void setListeners() { // these are set here as some have parameters, so for consistency they are all in one place
        playPromptButton.setOnAction(e -> {
            playPromptAudio();
            recordButton.setDisable(false);
            if (tutorialEnabled) {
                instructionLabel2.setVisible(true);
                recordButton.setVisible(true);
                stopButton.setVisible(true);
            }
        });

        recordButton.setOnAction(e -> {
            startRecording();
            recordButton.setDisable(true);
            stopButton.setDisable(false);
        });

        stopButton.setOnAction(e -> {
            stopRecording();
            stopButton.setDisable(true);
            playRecordingButton.setDisable(false);
            if (tutorialEnabled) {
                instructionLabel3.setVisible(true);
                playRecordingButton.setVisible(true);
            }
        });

        playRecordingButton.setOnAction(e -> {
            playRecordedAudio();
            recordButton.setDisable(false);
            nextAudioButton.setDisable(false);
            if (tutorialEnabled) {
                instructionLabel4.setVisible(true);
                nextAudioButton.setVisible(true);
            }
            if (lastExercise) {
                continueButton.setDisable(false);
            }
        });

        nextAudioButton.setOnAction(e -> {
            nextAudio();
            replay = false;
        });

        continueButton.setOnAction(e -> onContinueButtonClick());
    }

    private void decideExerciseContent(){
        // Step 1: load in data as usual
        List<String> data = Session.loadInExerciseDataForSection(learner.getProgress());

        // Step 2: generate the English and Italian phrases for the section based on the difficulty preference
        Session.generateContentForExercise(data);

        // Step 3: loading in audio items for section
        Listening listening = new Listening();
        getAudioItemsForSection(learner.getProgress());
    }

    private void playPromptAudio() {
        if (!audioItems.isEmpty()) {
            URL audioUrl = audioItems.get(timesContinuePressed).mediaURL;

            try {
                if (replay && player != null) {
                    // Seek to start and replay
                    player.seek(Duration.ZERO);
                    player.play();
                } else {
                    // Stop and dispose previous player if any
                    if (player != null) {
                        player.stop();
                        player.dispose();
                    }

                    Media media = new Media(audioUrl.toString());
                    player = new MediaPlayer(media);
                    player.play();
                    replay = true;
                }
            } catch (Exception e) {
                System.err.println("Error playing prompt: " + e.getMessage());
            }
        } else {
            System.err.println("No items left; this shouldn't be visible.");
        }
    }

    private void playRecordedAudio() {
        playAudio(recording.getAbsolutePath());
    }

    private void checkTutorialNeeded(int progress){
        if (progress == 1 && !tutorialEnabled){ // if very first time user has seen this exercise
            tutorialEnabled = true;
            startTutorial();
        } else {
            hideInstructions();
        }
    }

    private void startTutorial(){
        // firstly, hide everything
        instructionLabel2.setVisible(false); instructionLabel3.setVisible(false); instructionLabel4.setVisible(false);
        continueButton.setVisible(false); recordButton.setVisible(false); stopButton.setVisible(false);
        playRecordingButton.setVisible(false); nextAudioButton.setVisible(false);
    }

    private void hideInstructions(){
        instructionLabel1.setVisible(false);
        instructionLabel2.setVisible(false);
        instructionLabel3.setVisible(false);
        instructionLabel4.setVisible(false);
    }

    private void getAudioItemsForSection(int section) {
        List<String> italianPhrases = Session.getItalianPhrases();
        int difficulty = Session.getDifficultyPreference();

        try {
            URL folderURL = getClass().getResource("/audio/" + section);
            if (folderURL == null) {
                System.err.println("Folder not found: /audio/" + section);
                return;
            }

            Path folderPath = Paths.get(folderURL.toURI());

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*.m4a")) {
                for (Path file : stream) {
                    String fileName = file.getFileName().toString(); // e.g., legale.m4a
                    String phrase = fileName.replace(".m4a", "").trim();

                    if (italianPhrases.contains(phrase)) {
                        // Case 1: Phrase is in generated content so always add, bonusPhrase = false
                        URL fileUrl = getClass().getResource("/audio/" + section + "/" + fileName);
                        audioItems.add(new Speaking.AudioItem(fileUrl, false));
                    } else {
                        if (difficulty < 4) {
                            // Case 2: Easy mode, skip bonus phrase
                            continue;
                        }

                        // Case 3: Hard mode, check for corresponding .txt file to verify bonus phrase then add that
                        String textFilePath = "/audio/" + section + "/" + phrase + ".txt";
                        try (InputStream inputStream = getClass().getResourceAsStream(textFilePath);
                             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

                            URL fileUrl = getClass().getResource("/audio/" + section + "/" + fileName);
                            audioItems.add(new Speaking.AudioItem(fileUrl, true));
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void playAudio(String filename) {
        try {
            Media media = new Media(new File(filename).toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            player.play();
        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }

    private void startRecording() {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.err.println("Microphone not supported.");
                    continueButton.setVisible(true);
                    return;
                }

                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();
                outputStream = new ByteArrayOutputStream();
                isRecording = true;
                byte[] buffer = new byte[1024];
                while (isRecording) {
                    int count = microphone.read(buffer, 0, buffer.length);
                    outputStream.write(buffer, 0, count);
                }

                byte[] audioData = outputStream.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                AudioInputStream ais = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, recording);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void stopRecording() {
        isRecording = false;
        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
    }

    private void nextAudio(){
        timesContinuePressed += 1;
        resetUI();
        if (timesContinuePressed >= (audioItems.size() - 1)){
            nextAudioButton.setVisible(false);
            continueButton.setVisible(true);
            continueButton.setDisable(true);
            lastExercise = true;
        }
    }

    private void resetUI(){
        tutorialEnabled = false;
        hideInstructions();

        recordButton.setDisable(true);
        stopButton.setDisable(true);
        playRecordingButton.setDisable(true);
        nextAudioButton.setDisable(true);
    }

    @FXML
    private void onContinueButtonClick(){
        // since we have already used at least one exercise, we have to exclude any that have already been shown
        String next = getNext();

        Stage stage = (Stage) continueButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(next+".fxml")));

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setScene(newScene);

            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.setResizable(true);
            stage.setTitle("Langtrans Italiano");
            stage.centerOnScreen();

            Session.removeUsedExerciseFromRandomSelection(next);
        } catch (IOException e) {
            URL url = getClass().getResource("/com/example/javafxdemo/Exercises/"+next+".fxml");
            System.out.println("URL: " + url);
        }
    }

    private String getNext() {
        List<String> exercises = Session.getExercisesUnusedInSection();
        String next = "";
        if (exercises.isEmpty()){ // if no more exercises to show
            if (learner.getProgress() == 2) { //and it is time for the assessment (hardcoded here but still adaptable in future)
                next = "/com/example/javafxdemo/assessment_introduction"; // show the assessment intro
            } else {
                // if it is not time for the assessment then show next piece of content
                next = "/com/example/javafxdemo/content"; // including full filepath as it is one subfolder up
            }
        } else {
            Random random = new Random();
            int index = random.nextInt(exercises.size());
            next = exercises.get(index);
        }
        return next;
    }
}
