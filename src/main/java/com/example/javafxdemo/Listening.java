package com.example.javafxdemo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Listening {

    /**
     * old version of this class IN LITERATURE REVIEW FOLDER
     * old version ALSO CONTAINS EXTRA PSEUDOCODE @ BOTTOM
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
        Map<Integer, Set<String>> knownAnswers = loadKnownAnswers("context.txt");
        audioItems = getAudioItemsForSection(1, knownAnswers);
        audioItems.sort(Comparator.comparing(a -> a.isUnseen)); // make sure unseen is last

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
        feedbackLabel.setText("Answer: " + audioItems.get(currentIndex).expectedAnswer);
        giveUpButton.setVisible(false);
        nextAudio();
    }

    private void nextAudio() {
        currentIndex++;
        if (currentIndex < audioItems.size()) {
            userInput.clear();
            feedbackLabel.setText("");
            giveUpButton.setVisible(false);
            instructionLabel.setText(getInstructionForCurrentItem());
            playCurrentAudio();
        } else {
            feedbackLabel.setText("🎉 All done!");
            userInput.setDisable(true);
            checkButton.setDisable(true);
            replayButton.setDisable(true);
        }
    }

    private Map<Integer, Set<String>> loadKnownAnswers(String filename) {
        Map<Integer, Set<String>> known = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(filename))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                if (parts.length >= 12) {
                    int section = Integer.parseInt(parts[0].trim());
                    Set<String> answers = new HashSet<>();
                    answers.add(parts[9].trim());
                    answers.add(parts[10].trim());
                    answers.add(parts[11].trim());
                    known.put(section, answers);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return known;
    }


    private List<AudioItem> getAudioItemsForSection(int section, Map<Integer, Set<String>> knownAnswers) {
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
