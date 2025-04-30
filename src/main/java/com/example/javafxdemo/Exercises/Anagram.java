package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Exercise;
import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Anagram {

    @FXML private AnchorPane anchorPane;
    @FXML private TextField userInput;
    @FXML private Label instructionLabel, feedbackLabel, anagramLabel;
    @FXML private Button checkButton, giveUpButton, continueButton;
    @FXML private Button accentedAButton, accentedEButton, accentedIButton, accentedOButton, accentedUButton;
    @FXML private TextArea hint;
    @FXML private Button hintButton;

    private List<AnagramItem> items;
    private int currentIndex = 0;
    private int lastCaretPosition = 0;
    private String hintText;
    private boolean giveUpAlreadyAdded = false;

    private int phrasesCompleted = 0;

    Learner learner = Session.getLearner();

    private static class AnagramItem {
        String answer;
        String scrambled;
        boolean isUnseen;

        AnagramItem(String answer, boolean isUnseen) {
            this.answer = answer;
            this.scrambled = scramblePhrase(answer);
            this.isUnseen = isUnseen;
        }

        private static String scramblePhrase(String phrase) {
            String[] words = phrase.split(" ");
            StringBuilder scrambled = new StringBuilder();
            for (String word : words) {
                scrambled.append(scrambleWord(word)).append(" ");
            }
            return scrambled.toString().trim();
        }

        private static String scrambleWord(String word) {
            if (word.length() <= 2) return word;
            List<Character> chars = new ArrayList<>();
            for (char c : word.toCharArray()) chars.add(c);
            Collections.shuffle(chars);
            StringBuilder sb = new StringBuilder();
            for (char c : chars) sb.append(c);
            return sb.toString();
        }
    }

    public void initialize() {
        Map<Integer, Set<String>> knownAnswers = loadKnownAnswers(learner.getProgress(), "/com/example/javafxdemo/content.txt");
        items = getItemsForSection(learner.getProgress(), knownAnswers);
        items.sort(Comparator.comparing(a -> a.isUnseen)); // show unseen first

        setupUI();
        updateDisplay();
        setListeners();
        Session.startColorCycle(anchorPane);
    }

    private void setupUI() {
        feedbackLabel.setText("");
        giveUpButton.setVisible(false);
        hint.setVisible(false);
        hintButton.setVisible(true);
        continueButton.setVisible(false);
    }

    private void setListeners() {
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

    private void insertAtCaret(String character) {
        String currentText = userInput.getText();
        int caretPositionToReturnTo = lastCaretPosition;
        String newText = currentText.substring(0, lastCaretPosition) + character + currentText.substring(lastCaretPosition);
        userInput.setText(newText);
        userInput.requestFocus();
        userInput.positionCaret(caretPositionToReturnTo + character.length());
        lastCaretPosition = caretPositionToReturnTo + character.length();
    }

    private void updateDisplay() {
        instructionLabel.setText(getInstruction());
        anagramLabel.setText(items.get(currentIndex).scrambled);
        enableAccentMarkButtonsWhereNecessary(items.get(currentIndex).answer);
    }

    private String getInstruction() {
        return items.get(currentIndex).isUnseen
                ? "Unscramble the phrase: \n\n It's okay to get it wrong!"
                : "Unscramble the phrase you see:";
    }

    public void showHint() {
        hint.setVisible(true);
        hint.setEditable(false);
        hint.setFocusTraversable(false);
        hint.setText(hintText);
        hintButton.setVisible(false);
        hint.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;" +
                "-fx-control-inner-background: transparent; -fx-font-size: 25px;");
    }

    @FXML
    public void onCheck() {
        String userAnswer = userInput.getText().trim().toLowerCase();
        String correctAnswer = items.get(currentIndex).answer.toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            feedbackLabel.setText("✅ Correct!");
            feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: green;");
            giveUpButton.setVisible(false);
            phrasesCompleted += 1;
            showContinueIfDone();
            nextItem();
        } else {
            feedbackLabel.setText("❌ Try again!");
            feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: red;");
            giveUpButton.setVisible(true);
        }
    }

    @FXML
    public void onGiveUp() {
        feedbackLabel.setText("The correct answer was: \n" + items.get(currentIndex).answer);
        feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: orange;");
        giveUpButton.setVisible(false);
        phrasesCompleted += 1;
        showContinueIfDone();
        nextItem();

        if (!giveUpAlreadyAdded) {
            Session.addGiveUp(new Exercise("Anagram", learner.getProgress()));
            giveUpAlreadyAdded = true;
        }
    }

    private void nextItem() {
        currentIndex++;
        if (currentIndex < items.size()) {
            userInput.clear();
            updateDisplay();
        } else {
            userInput.setDisable(true);
            checkButton.setDisable(true);
        }
    }

    private void enableAccentMarkButtonsWhereNecessary(String expectedAnswer) {
        accentedAButton.setVisible(expectedAnswer.contains("à"));
        accentedEButton.setVisible(expectedAnswer.contains("è"));
        accentedIButton.setVisible(expectedAnswer.contains("ì"));
        accentedOButton.setVisible(expectedAnswer.contains("ò"));
        accentedUButton.setVisible(expectedAnswer.contains("ù"));
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

    private List<AnagramItem> getItemsForSection(int section, Map<Integer, Set<String>> knownAnswers) {
        List<AnagramItem> items = new ArrayList<>();
        Set<String> known = knownAnswers.getOrDefault(section, new HashSet<>());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/javafxdemo/content.txt"))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                int contentSection = Integer.parseInt(parts[0].trim());
                if (contentSection == section) {
                    for (int i = 7; i <= 9; i++) {
                        String phrase = parts[i].trim();
                        boolean isUnseen = !known.contains(phrase);
                        items.add(new AnagramItem(phrase, isUnseen));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void showContinueIfDone(){
        if (phrasesCompleted == 3){
            continueButton.setVisible(true);
            hint.setVisible(false);
            hintButton.setVisible(false);
        }
    }

    @FXML
    private void onContinueButtonClick() {
        List<String> exercises = Session.getExercisesUnusedInSection();
        String next = "";
        if (exercises.isEmpty()) {
            next = "/com/example/javafxdemo/content";
        } else {
            Random random = new Random();
            int index = random.nextInt(exercises.size());
            next = exercises.get(index);
        }

        Stage stage = (Stage) continueButton.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(next + ".fxml")));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.setMaximized(true);
            stage.setTitle("Langtrans Italiano");
            stage.centerOnScreen();
            Session.removeUsedExerciseFromRandomSelection(next);
        } catch (IOException e) {
            URL url = getClass().getResource("/com/example/javafxdemo/Exercises/" + next + ".fxml");
            System.out.println("URL: " + url);
        }
    }
}

