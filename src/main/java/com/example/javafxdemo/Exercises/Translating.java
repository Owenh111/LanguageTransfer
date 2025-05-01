package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Exercise;
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
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;


public class Translating {

    @FXML private AnchorPane anchorPane;
    // Text fields for answers
    @FXML private TextField answerField1;
    @FXML private TextField answerField2;
    @FXML private TextField answerField3;

    // Buttons for Check and Give Up
    @FXML private Label question1;
    @FXML private Button checkButton1;
    @FXML private Button giveUpButton1;

    @FXML private Label question2;
    @FXML private Button checkButton2;
    @FXML private Button giveUpButton2;

    @FXML private Label question3;
    @FXML private Button checkButton3;
    @FXML private Button giveUpButton3;

    @FXML private Label feedbackLabel1, feedbackLabel2, feedbackLabel3;

    @FXML private Button accentedAButton, accentedEButton, accentedIButton, accentedOButton, accentedUButton;

    @FXML private Button continueButton, hintButton;

    @FXML private TextArea hint;

    Learner learner = Session.getLearner();

    Integer sectionToLoad = learner.getProgress();
    ArrayList<String> answers = new ArrayList<>();

    Boolean answer1Correct = false;
    Boolean answer2Correct = false;
    Boolean answer3Correct = false;
    private TextField lastFocusedField;
    private int lastCaretPosition = 0;
    private String hintText;
    private Boolean giveUpAlreadyAdded = false;

    public void initialize() throws IOException {
        Session.startColorCycle(anchorPane);
        readInData();
        setUpLabels();
        enableAccentMarkButtonsIfNecessary();

        setListeners();
    }

    public void setListeners(){
        answerField1.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                lastFocusedField = answerField1;
                lastCaretPosition = answerField1.getCaretPosition();
            }
        });

        answerField2.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                lastFocusedField = answerField2;
                lastCaretPosition = answerField2.getCaretPosition();
            }
        });

        answerField3.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                lastFocusedField = answerField3;
                lastCaretPosition = answerField3.getCaretPosition();
            }
        });

        answerField1.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            if (answerField1.isFocused()) {
                lastCaretPosition = newVal.intValue();
            }
        });
        answerField2.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            if (answerField2.isFocused()) {
                lastCaretPosition = newVal.intValue();
            }
        });
        answerField3.caretPositionProperty().addListener((obs, oldVal, newVal) -> {
            if (answerField3.isFocused()) {
                lastCaretPosition = newVal.intValue();
            }
        });

        accentedAButton.setOnAction(e -> insertAtCaret("à"));
        accentedEButton.setOnAction(e -> insertAtCaret("è"));
        accentedIButton.setOnAction(e -> insertAtCaret("ì"));
        accentedOButton.setOnAction(e -> insertAtCaret("ò"));
        accentedUButton.setOnAction(e -> insertAtCaret("ù"));
    }

    public void readInData(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/javafxdemo/content.txt"))))) {
            String line;
            String[] parts;
            while ((line = reader.readLine()) != null) {
                parts = line.split("`");
                if (parts.length == 13) {
                    int section = Integer.parseInt(parts[0].trim());
                    if (section == sectionToLoad) {
                        answers.add(parts[4].trim());
                        answers.add(parts[5].trim());
                        answers.add(parts[6].trim());
                        hintText = parts[12];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo: change below to match what i've written in notebook: will need to make a method for translating to Italian OR from Italian
    /**
    private List<Anagram.AnagramItem> loadAndGenerateItemsForSection(int sectionToLoad) {
        List<Anagram.AnagramItem> items = new ArrayList<>();

        // Step 1: Load exercise data for the given section and save hint text
        List<String> data = Session.loadInExerciseDataForSection(sectionToLoad);

        hintText = Session.getHint(data);

        // Step 2: Generate the English and Italian phrases for the section based on the difficulty preference
        Pair<List<String>, List<String>> generatedContent = Session.generateContentForExercise(data);

        // Step 3: Add the generated English phrases to known answers
        Set<String> answers = new HashSet<>();
        answers.addAll(generatedContent.getValue()); // Italian phrases

        // Step 4: Generate AnagramItems

        for (String italianPhrase : answers) {
            items.add(new Anagram.AnagramItem(italianPhrase));
        }

        return items;
    }
*/
    //todo: check & fix method below
    private void assignLabelsAndAnswers(List<String> englishPhrases, List<String> italianPhrases) {
        List<String> labels = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        int difficulty = Session.getDifficultyPreference();

        switch (difficulty) {
            case 1:
                // Labels: Italian, Answers: English
                labels.addAll(italianPhrases);
                answers.addAll(englishPhrases);
                break;

            case 2:
                // 2 Italian -> English, 1 English -> Italian
                labels.add(italianPhrases.get(0));
                answers.add(englishPhrases.get(0));

                labels.add(italianPhrases.get(1));
                answers.add(englishPhrases.get(1));

                labels.add(englishPhrases.get(2));
                answers.add(italianPhrases.get(2));
                break;

            case 3:
                // 2 English -> Italian, 1 Italian -> English
                labels.add(englishPhrases.get(0));
                answers.add(italianPhrases.get(0));

                labels.add(englishPhrases.get(1));
                answers.add(italianPhrases.get(1));

                labels.add(italianPhrases.get(2));
                answers.add(englishPhrases.get(2));
                break;

            case 4:
            case 5:
                // Labels: English, Answers: Italian (opposite of case 1)
                labels.addAll(englishPhrases);
                answers.addAll(italianPhrases);
                break;
        }
    }

    public void setUpLabels(){
        question1.setText(answers.get(0));
        question2.setText(answers.get(1));
        question3.setText(answers.get(2));
    }

    public void enableAccentMarkButtonsIfNecessary() throws IOException {

        Integer answerIndex = 7;
        while (answerIndex < 10) {
            Pair<Character, String> result = checkAnswer("",answerIndex);

            accentedAButton.setVisible(result.getValue().contains("à"));
            accentedEButton.setVisible(result.getValue().contains("è"));
            accentedIButton.setVisible(result.getValue().contains("ì"));
            accentedOButton.setVisible(result.getValue().contains("ò"));
            accentedUButton.setVisible(result.getValue().contains("ù"));

            answerIndex+=1;
        }
    }

    private void insertAtCaret(String character) {
        if (lastFocusedField != null) {
            String currentText = lastFocusedField.getText();

            int caretPositionToReturnTo = lastCaretPosition;
            // Insert the character at the saved caret position
            String newText = currentText.substring(0, lastCaretPosition) + character + currentText.substring(lastCaretPosition);

            lastFocusedField.setText(newText);

            // Move caret to after inserted character
            lastFocusedField.requestFocus();
            lastFocusedField.positionCaret(caretPositionToReturnTo + character.length());

            // Update lastCaretPosition (since we inserted a character)
            lastCaretPosition = caretPositionToReturnTo + character.length();
        } else {
            System.out.println("No field selected!");
        }
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

    public Pair<Character, String> checkAnswer(String userAnswer, Integer answerIndex) throws IOException {
        String trueAnswer = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/javafxdemo/content.txt"))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                int section = Integer.parseInt(parts[0].trim());
                if (section == sectionToLoad) {
                    trueAnswer = parts[answerIndex];
                }
            }
        }
        if (Objects.equals(trueAnswer, userAnswer)) {
            return new Pair<>('y', trueAnswer);
        } else {
            return new Pair<>('n', trueAnswer);
        }
    }

    public void checkIfContinueShouldEnable(){
        if (answer1Correct && answer2Correct && answer3Correct){ // answerCorrect == true can be simplified to answerCorrect
            continueButton.setDisable(false);
            hint.setVisible(false);
            hintButton.setVisible(false);
        }
    }

    public void onCheck1(ActionEvent event) {
        String answerFieldContent = answerField1.getText();
        Pair<Character, String> result = new Pair<>('?', "");

        if (!answerFieldContent.isEmpty()){
            try {
                result = checkAnswer(answerFieldContent, 7);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (result.getKey() == 'y') {
            feedbackLabel1.setText("✔ Correct");
            feedbackLabel1.setStyle("-fx-font-size: 40px; -fx-text-fill: green;");
            answerField1.setEditable(false);
            checkButton1.setDisable(true);
            giveUpButton1.setDisable(true);
            answer1Correct = true;
            checkIfContinueShouldEnable();
        } else if (result.getKey() == 'n'){
            feedbackLabel1.setText("❌ Incorrect");
            feedbackLabel1.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");
        } else {
            feedbackLabel1.setText("❗ Nothing to check!");
            feedbackLabel1.setStyle("-fx-font-size: 40px; -fx-text-fill: orange;");
        }
    }

    public void onGiveUp1(ActionEvent event) {
        String answerFieldContent = answerField1.getText();
        Pair<Character, String> result;

        try {
            result = checkAnswer(answerFieldContent, 7);
            answerField1.setText("The answer was \"" + result.getValue() + "\"");
            answerField1.setEditable(false);
            checkButton1.setDisable(true);
            giveUpButton1.setDisable(true);
            feedbackLabel1.setText("\uD83D\uDEAB Question skipped");
            feedbackLabel1.setStyle("-fx-font-size: 40px; -fx-text-fill: orange;");
            answer1Correct = true;
            checkIfContinueShouldEnable();

            if (!giveUpAlreadyAdded){
                Session.addGiveUp(new Exercise("Translating",learner.getProgress()));
                giveUpAlreadyAdded = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCheck2(ActionEvent event) {
        String answerFieldContent = answerField2.getText();
        Pair<Character, String> result = new Pair<>('?', "");
        if (!answerFieldContent.isEmpty()){
            try {
                result = checkAnswer(answerFieldContent, 8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (result.getKey() == 'y') {
            feedbackLabel2.setText("✔ Correct");
            feedbackLabel2.setStyle("-fx-font-size: 40px; -fx-text-fill: green;");
            answerField2.setEditable(false);
            checkButton2.setDisable(true);
            giveUpButton2.setDisable(true);
            answer2Correct = true;
            checkIfContinueShouldEnable();
        } else if (result.getKey() == 'n'){
            feedbackLabel2.setText("❌ Incorrect");
            feedbackLabel2.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");
        } else {
            feedbackLabel2.setText("❗ Nothing to check!");
            feedbackLabel2.setStyle("-fx-font-size: 40px; -fx-text-fill: orange;");
        }
    }

    public void onGiveUp2(ActionEvent event) {
        String answerFieldContent = answerField2.getText();
        Pair<Character, String> result;

        try {
            result = checkAnswer(answerFieldContent, 8);
            answerField2.setText(("The answer was \"" + result.getValue() + "\""));
            answerField2.setEditable(false);
            checkButton2.setDisable(true);
            giveUpButton2.setDisable(true);
            feedbackLabel2.setText("\uD83D\uDEAB Question skipped");
            feedbackLabel2.setStyle("-fx-font-size: 40px; -fx-text-fill: orange;");
            answer2Correct = true;
            checkIfContinueShouldEnable();

            if (!giveUpAlreadyAdded){
                Session.addGiveUp(new Exercise("Translating",learner.getProgress()));
                giveUpAlreadyAdded = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCheck3(ActionEvent event) {
        String answerFieldContent = answerField3.getText();
        Pair<Character, String> result = new Pair<>('?', "");
        if (!answerFieldContent.isEmpty()){
            try {
                result = checkAnswer(answerFieldContent, 9);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (result.getKey() == 'y') {
            feedbackLabel3.setText("✔ Correct");
            feedbackLabel3.setStyle("-fx-font-size: 40px; -fx-text-fill: green;");
            answerField3.setEditable(false);
            checkButton3.setDisable(true);
            giveUpButton3.setDisable(true);
            answer3Correct = true;
            checkIfContinueShouldEnable();
        } else if (result.getKey() == 'n'){
            feedbackLabel3.setText("❌ Incorrect");
            feedbackLabel3.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");
        } else {
            feedbackLabel3.setText("❗ Nothing to check!");
            feedbackLabel3.setStyle("-fx-font-size: 40px; -fx-text-fill: orange;");
        }
    }

    public void onGiveUp3(ActionEvent event) {
        String answerFieldContent = answerField2.getText();
        Pair<Character, String> result;

        try {
            result = checkAnswer(answerFieldContent, 9);
            answerField3.setText("The answer was \"" + result.getValue() + "\"");
            answerField3.setEditable(false);
            checkButton3.setDisable(true);
            giveUpButton3.setDisable(true);
            feedbackLabel3.setText("\uD83D\uDEAB Question skipped");
            feedbackLabel3.setStyle("-fx-font-size: 40px; -fx-text-fill: orange;");
            answer3Correct = true;
            checkIfContinueShouldEnable();

            if (!giveUpAlreadyAdded){
                Session.addGiveUp(new Exercise("Translating",learner.getProgress()));
                giveUpAlreadyAdded = true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onContinueButtonClick(){
        hint.setVisible(false);
        // since we have already used at least one exercise, we have to exclude any that have already been shown
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
