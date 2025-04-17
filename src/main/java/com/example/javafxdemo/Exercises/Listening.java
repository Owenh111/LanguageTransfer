package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Learner;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.*;
import javafx.util.Duration;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class Listening {

    /**
     * old version of this class IN LITERATURE REVIEW FOLDER
     * old version ALSO CONTAINS EXTRA PSEUDOCODE @ BOTTOM
     * (probably not needed anymore)
     */
    @FXML private MediaView mediaView;
    @FXML private TextField userInput;
    @FXML private Label instructionLabel;
    @FXML private Label feedbackLabel;
    @FXML private Button checkButton, replayButton, giveUpButton;

    private List<AudioItem> audioItems;
    private int currentIndex = 0;
    private MediaPlayer currentPlayer;

    public void initialize() {
        Map<Integer, Set<String>> knownAnswers = loadKnownAnswers("/com/example/javafxdemo/content.txt");
        audioItems = getAudioItemsForSection(1, knownAnswers);
        // TODO: 17/04/2025  above is hardcoded, needs to be dynamic by fixing and then calling getProgress
        audioItems.sort(Comparator.comparing(a -> a.isUnseen)); // moves unseen to last
        
        setupUI();
        playCurrentAudio();
    }

    private void setupUI() {
        feedbackLabel.setText("");
        giveUpButton.setVisible(false);
        instructionLabel.setText(getInstructionForCurrentItem());
    }

    private String getInstructionForCurrentItem() {
        return audioItems.get(currentIndex).isUnseen
                ? "Type what you hear as closely as possible:"
                : "Type the phrase you heard:";
        // TODO: 17/04/2025 understand how this works 
    }

    private void playCurrentAudio() {
        if (currentPlayer != null) currentPlayer.dispose();
        Media media = new Media(audioItems.get(currentIndex).mediaURL.toString());
        currentPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(currentPlayer);
        currentPlayer.play();
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
            showDefinition(filename);
        }
        
        if (userAnswer.equals(correctAnswer)) {
            feedbackLabel.setText("✅ Correct!");
            giveUpButton.setVisible(false);
            nextAudio();
        } else {
            feedbackLabel.setText("❌ Try again!");
            giveUpButton.setVisible(true);
        }
    }

    @FXML
    public void onGiveUp() {
        feedbackLabel.setText("The previous answer was: " + audioItems.get(currentIndex).expectedAnswer);
        if (audioItems.get(currentIndex).isUnseen){
            String filename = audioItems.get(currentIndex).expectedAnswer + ".txt";
            showDefinition(filename);
        }
        giveUpButton.setVisible(false);
        nextAudio();
    }

    private void showDefinition(String filename){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/audio/" + 1 + "/" + filename))))) {
            // TODO: 17/04/2025 un-hardcode the above, needs progress Int to be taken dynamically 
            String line;
            if ((line = reader.readLine()) != null) {
                instructionLabel.setText("definition: " + line);
                // TODO: 17/04/2025 doesnt matter much but if definition spans multiple lines only 1st will be read 
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    private void nextAudio() {
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
        }
    }

    private Map<Integer, Set<String>> loadKnownAnswers(String filename) {
        // TODO: 17/04/2025 reads all in at once, whereas when progress is passed dynamically 
        // TODO: 17/04/2025 this needs to be rewritten to only load the section with the same Int value 
        Map<Integer, Set<String>> known = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(filename))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                if (parts.length >= 11) {
                    int section = Integer.parseInt(parts[0].trim());
                    Set<String> answers = new HashSet<>();
                    answers.add(parts[7].trim());
                    answers.add(parts[8].trim());
                    answers.add(parts[9].trim());
                    known.put(section, answers);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return known;
    }


    private List<AudioItem> getAudioItemsForSection(int section, Map<Integer, Set<String>> knownAnswers) {
        // TODO: 17/04/2025 understand/comment this properly for the VIVA
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
                    items.add(new AudioItem(phrase, fileUrl, isUnseen));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
    
    private int getProgress(){
        // TODO: 17/04/2025 getProgress function; currently returns placeholder 
        // TODO: 17/04/2025 will need to mess around with Learner class to get Int w/o passing it
        return 1;
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
}
