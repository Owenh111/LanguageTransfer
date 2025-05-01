package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Exercise;
import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class Listening {

    @FXML private AnchorPane anchorPane;
    @FXML private MediaView mediaView;
    @FXML private TextField userInput;
    @FXML private Label instructionLabel, feedbackLabel;
    @FXML private Button checkButton, replayButton, giveUpButton, continueButton;
    @FXML private Button accentedAButton, accentedEButton, accentedIButton, accentedOButton, accentedUButton;
    @FXML private TextArea hint;
    @FXML private Button hintButton;

    private List<AudioItem> audioItems;
    private int currentIndex = 0;
    private MediaPlayer currentPlayer;

    Learner learner = Session.getLearner();
    private int lastCaretPosition = 0;
    private String hintText;

    private Boolean giveUpAlreadyAdded = false;

    public void initialize() {
        Map<Integer, Set<String>> knownAnswers = loadKnownAnswers(learner.getProgress(), "/com/example/javafxdemo/content.txt");
        audioItems = getAudioItemsForSection(learner.getProgress(), knownAnswers);
        audioItems.sort(Comparator.comparing(a -> a.isUnseen)); // moves unseen item to last
        
        setupUI();
        playCurrentAudio();
        setListeners();

        Session.startColorCycle(anchorPane);
    }

    public void setListeners() {
        userInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                lastCaretPosition = userInput.getCaretPosition();
            }
        });

        userInput.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            if (userInput.isFocused()) {
                lastCaretPosition = newVal.intValue();
            }
        });

        accentedAButton.setOnAction(e -> insertAtCaret("à"));
        accentedEButton.setOnAction(e -> insertAtCaret("è"));
        accentedIButton.setOnAction(e -> insertAtCaret("ì"));
        accentedOButton.setOnAction(e -> insertAtCaret("ò"));
        accentedUButton.setOnAction(e -> insertAtCaret("ù"));
    }

    private void setupUI() {
        feedbackLabel.setText("");
        giveUpButton.setVisible(false);
        instructionLabel.setText(getInstructionForCurrentItem());
    }

    private static class AudioItem {
        String expectedAnswer;
        URL mediaURL;
        boolean isUnseen;

        AudioItem(String answer, URL mediaURL, boolean isUnseen) {
            this.expectedAnswer = answer;
            this.mediaURL = mediaURL;
            this.isUnseen = isUnseen;
        }
    }

    private void insertAtCaret(String character) {
        String currentText = userInput.getText();

        int caretPositionToReturnTo = lastCaretPosition;
        // Insert the character at the saved caret position
        String newText = currentText.substring(0, lastCaretPosition) + character + currentText.substring(lastCaretPosition);

        userInput.setText(newText);

        // Move caret to after inserted character
        userInput.requestFocus();
        userInput.positionCaret(caretPositionToReturnTo + character.length());

        // Update lastCaretPosition (since we inserted a character)
        lastCaretPosition = caretPositionToReturnTo + character.length();
    }

    private String getInstructionForCurrentItem() {
        return audioItems.get(currentIndex).isUnseen
                ? "Type what you hear as closely as possible: \n\n Go for it - no worries if you get it wrong!"
                : "Type the phrase you heard: \n\n There is a replay button if you need it! \n\n You can pass the question after one failed attempt.";
    }

    private void playCurrentAudio() {
        if (currentPlayer != null) currentPlayer.dispose();
        Media media = new Media(audioItems.get(currentIndex).mediaURL.toString());
        currentPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(currentPlayer);

        enableAccentMarkButtonsWhereNecessary(audioItems.get(currentIndex).expectedAnswer);

        currentPlayer.play();
    }

    public void showHint(){
        hint.setVisible(true);
        hint.setEditable(false);
        hint.setFocusTraversable(false);
        hint.setText(hintText);
        hintButton.setVisible(false);
        hint.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;" +
                "-fx-control-inner-background: transparent; -fx-font-size: 25px;");
    }

    @FXML
    public void onReplay() {
        if (currentPlayer != null) {
            currentPlayer.seek(Duration.ZERO);
            currentPlayer.play();
        }
    }

    @FXML
    public void onCheck() {
        String userAnswer = userInput.getText().trim().toLowerCase();
        String correctAnswer = audioItems.get(currentIndex).expectedAnswer.toLowerCase();

        if (audioItems.get(currentIndex).isUnseen){
            String filename = audioItems.get(currentIndex).expectedAnswer + ".txt";
            if (userAnswer.equals(correctAnswer)) {
                showDefinition(filename);
            }
        }
        
        if (userAnswer.equals(correctAnswer)) {
            feedbackLabel.setText("✅ Correct!");
            feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: green;");
            giveUpButton.setVisible(false);
            nextAudioOrNextExercise();
        } else {
            feedbackLabel.setText("❌ Try again!");
            feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: red;");
            giveUpButton.setVisible(true);
        }
    }

    @FXML
    public void onGiveUp() {
        feedbackLabel.setText("The previous answer was: " + audioItems.get(currentIndex).expectedAnswer);
        feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: orange;");
        if (audioItems.get(currentIndex).isUnseen){
            String filename = audioItems.get(currentIndex).expectedAnswer + ".txt";
            showDefinition(filename);
        }
        giveUpButton.setVisible(false);
        nextAudioOrNextExercise();

        if (!giveUpAlreadyAdded){
            Session.addGiveUp(new Exercise("Listening",learner.getProgress()));
            giveUpAlreadyAdded = true;
        }
    }

    private void showDefinition(String filename){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/audio/" + learner.getProgress() + "/" + filename))))) {
            String line;
            if ((line = reader.readLine()) != null) {
                instructionLabel.setText("definition: " + line);
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    private void nextAudioOrNextExercise() {
        currentIndex++;
        if (currentIndex < audioItems.size()) {
            userInput.clear();
            giveUpButton.setVisible(false);
            instructionLabel.setText(getInstructionForCurrentItem());
            playCurrentAudio();
        } else {
            userInput.setDisable(true);
            checkButton.setDisable(true);
            replayButton.setDisable(true);
            continueButton.setVisible(true);
            hint.setVisible(false);
            hintButton.setVisible(false);
        }
    }

    private void enableAccentMarkButtonsWhereNecessary(String expectedAnswer) {
        boolean hasGraveA = false, hasGraveE = false, hasGraveI = false, hasGraveO = false, hasGraveU = false;


            if (expectedAnswer.contains("à")) hasGraveA = true;
            if (expectedAnswer.contains("è")) hasGraveE = true;
            if (expectedAnswer.contains("ì")) hasGraveI = true;
            if (expectedAnswer.contains("ò")) hasGraveO = true;
            if (expectedAnswer.contains("ù")) hasGraveU = true;


        accentedAButton.setVisible(hasGraveA);
        accentedEButton.setVisible(hasGraveE);
        accentedIButton.setVisible(hasGraveI);
        accentedOButton.setVisible(hasGraveO);
        accentedUButton.setVisible(hasGraveU);
    }


    private Map<Integer, Set<String>> loadKnownAnswers(Integer sectionToLoad, String filename) {
        Map<Integer, Set<String>> known = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(filename))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                    int section = Integer.parseInt(parts[0].trim());
                    if (section == sectionToLoad) {
                        Set<String> answers = new HashSet<>();
                        answers.add(parts[7].trim());
                        answers.add(parts[8].trim());
                        answers.add(parts[9].trim());

                        hintText = parts[12];
                        known.put(section, answers);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return known;
    }


    private List<AudioItem> getAudioItemsForSection(int section, Map<Integer, Set<String>> knownAnswers) {
        // TODO: 17/04/2025 comment this properly for the VIVA
        List<AudioItem> items = new ArrayList<>();
        try {
            URL folderURL = getClass().getResource("/audio/" + section);
            if (folderURL == null) {
                System.err.println("Folder not found: /audio/" + section);
                return items;
            }

            URI folderURI = folderURL.toURI();
            Path folderPath = Paths.get(folderURI);

            Set<String> known = knownAnswers.getOrDefault(section, new HashSet<>());

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*.mp3")) {
                for (Path file : stream) {
                    String name = file.getFileName().toString(); // e.g. pronunciation_it_legale.mp3
                    String phrase = name.replace("pronunciation_it_", "")
                            .replace(".mp3", "")
                            .replace("_", " ")
                            .trim();

                    boolean isUnseen = !known.contains(phrase);
                    URL fileUrl = getClass().getResource("/audio/" + section + "/" + name);
                    if (Session.getDifficultyPreference() < 3){ //if the user is playing on an easier mode...
                        if (isUnseen == true){ //...and if this is an unseen (hard) phrase
                            //...then do nothing as this will exclude the hard exercise
                        } else {
                            items.add(new AudioItem(phrase, fileUrl, isUnseen));
                        }
                    } else {
                        items.add(new AudioItem(phrase, fileUrl, isUnseen));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @FXML
    private void onContinueButtonClick(){
        //since we have already used at least one exercise, we have to exclude any that have already been shown
        List<String> exercises = Session.getExercisesUnusedInSection();
        String next = "";
        if (exercises.isEmpty()){
            // if no more exercises to show then show next piece of content
            next = "/com/example/javafxdemo/content"; // including full filepath as it is one subfolder up
        } else {
            Random random = new Random();
            int index = random.nextInt(exercises.size());
            next = exercises.get(index);
        }

        Stage stage = (Stage) continueButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(next+".fxml")));

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setScene(newScene);

            stage.setMaximized(true);
            stage.setTitle("Langtrans Italiano");
            stage.centerOnScreen();

            Session.removeUsedExerciseFromRandomSelection(next);
        } catch (IOException e) {
            URL url = getClass().getResource("/com/example/javafxdemo/Exercises/"+next+".fxml");
            System.out.println("URL: " + url);
        }
    }
}
