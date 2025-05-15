package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Exercise;
import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

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

        AnagramItem(String answer) {
            this.answer = answer;
            this.scrambled = scramblePhrase(answer);
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
        items = loadAndGenerateItemsForSection(learner.getProgress());

        setupUI();
        updateDisplay();
        setListeners();
        Session.startColorCycle(anchorPane);
    }

    private List<AnagramItem> loadAndGenerateItemsForSection(int sectionToLoad) {
        List<AnagramItem> items = new ArrayList<>();

        // Step 1: Load exercise data for the given section and save hint text
        List<String> data = Session.loadInExerciseDataForSection(sectionToLoad);

        hintText = Session.getHint(data);

        // Step 2: Generate the English and Italian phrases for the section based on the difficulty preference
        Pair<List<String>, List<String>> generatedContent = Session.generateContentForExercise(data);

        // Step 3: Add the generated English phrases to known answers
        Set<String> answers = new HashSet<>();
        answers.addAll(generatedContent.getValue()); // Italian Phrases

        // Step 4: Generate AnagramItems

        for (String italianPhrase : answers) {
            items.add(new AnagramItem(italianPhrase));
        }

        return items;
    }

    private void setupUI() {
        feedbackLabel.setText("");
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
        instructionLabel.setText("Unscramble the Italian phrase you see!");
        anagramLabel.setText(items.get(currentIndex).scrambled);
        enableAccentMarkButtonsWhereNecessary(items.get(currentIndex).answer);
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
            phrasesCompleted += 1;
            showContinueIfDone();
            nextItem();
        } else {
            feedbackLabel.setText("❌ Try again!");
            feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: red;");
        }
    }

    @FXML
    public void onGiveUp() {
        feedbackLabel.setText("The correct answer was: \n" + items.get(currentIndex).answer);
        feedbackLabel.setStyle("-fx-font-size: 55px; -fx-text-fill: orange;");
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

    private void showContinueIfDone(){
        if (phrasesCompleted == 3){
            continueButton.setVisible(true);
            hint.setVisible(false);
            hintButton.setVisible(false);
            giveUpButton.setDisable(true);
        }
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

