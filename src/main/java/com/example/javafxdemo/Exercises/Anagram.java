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
    @FXML
    TextField userInput;
    @FXML private Label instructionLabel;
    @FXML Label feedbackLabel;
    @FXML private Label anagramLabel;
    @FXML private Button checkButton, giveUpButton, continueButton;
    @FXML private Button accentedAButton, accentedEButton, accentedIButton, accentedOButton, accentedUButton;
    @FXML private TextArea hint;
    @FXML private Button hintButton;

    private List<AnagramItem> items;
    int currentIndex = 0;
    private int lastCaretPosition = 0; // caret is another term for the cursor that you see when entering text
    private String hintText;
    private boolean giveUpAlreadyAdded = false;

    int phrasesCompleted = 0;

    Learner learner = Session.getLearner();

    static class AnagramItem { // for this exercise we have an AnagramItem, which is static for easier referencing
        String answer; // the unscrambled answer
        String scrambled; // the scrambled version of the answer

        AnagramItem(String answer) {
            this.answer = answer;
            this.scrambled = scramblePhrase(answer);
        }

        /**
         * it is technically possible that scrambling the phrase returns the same phrase as the correct one
         * this could be changed reasonably easily by just redoing it if the two strings match
         * all the static stuff needs to go at the top otherwise no code that comes before that can call it
         * @param phrase
         * @return
         */
        private static String scramblePhrase(String phrase) { // this puts all the scrambled words in the right order
            String[] words = phrase.split(" "); // keep all words separate so that words are scrambled individually
            StringBuilder scrambled = new StringBuilder(); // i.e. a mutable version of a String
            for (String word : words) { // for each word in the phrase, which has now been split and had spaces removed
                scrambled.append(scrambleWord(word)).append(" "); // after scrambling add the spaces back in
            }
            if (phrase.contentEquals(scrambled)){

            }
            return scrambled.toString().trim(); // return the whole phrase, all stitched together
        }

        private static String scrambleWord(String word) {
            List<Character> chars = new ArrayList<>();
            for (char c : word.toCharArray()) {
                chars.add(c); // turns the word into an array of chars, e.g. "hello" goes to 'h','e','l','l','o'
            }

            Collections.shuffle(chars); // inbuilt static method that pseudorandomly randomises the order of the array
            StringBuilder sb = new StringBuilder(); // another mutable StringBuilder, like we made earlier

            for (char c : chars) {
                sb.append(c); // add each char onto the end to create a new string
            }

            return sb.toString(); // make the immutable StringBuilder into an actual String
        }
    }

    public void initialize() {
        items = loadAndGenerateItemsForSection(learner.getProgress());

        setupUI();
        updateDisplay();
        setListeners();

        if (Session.inAssessmentMode()) {
            feedbackLabel.setVisible(false); // we exclude feedback for assessments
        }

        Session.startColourCycle(anchorPane);
    }

    public List<AnagramItem> loadAndGenerateItemsForSection(int sectionToLoad) {
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
        // although the listeners below have obs and oldVal which are not called, they are required as parameters anyway
        userInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                lastCaretPosition = userInput.getCaretPosition(); // return cursor to where it was in text field
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
        // i.e. find where the cursor is and insert the character between what was before and after
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
            if (!Session.inAssessmentMode()) {
                Session.addGiveUp(new Exercise("Anagram", learner.getProgress()));
                giveUpAlreadyAdded = true;
            }
        }

        if (Session.inAssessmentMode()){
            Session.addSkippedAnswer();
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
            URL url = getClass().getResource(next+".fxml");
            System.out.println("URL: " + url);
        }
    }

    private String getNext() {
        List<String> exercises = Session.getExercisesUnusedInSection();
        String next = "";
        if (!Session.inAssessmentMode()) { // if we are in learning mode
            if (exercises.isEmpty()) { // if no more exercises to show
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
        } else { // if we are in assessment mode
            if (!Session.assessmentComplete()){ // if we still need to load more stuff for the assessment
                List<Exercise> assessmentData = Session.getAssessmentData();
                Exercise exerciseToLoad = assessmentData.get(Session.getAssessmentIndex()); // tracks no. of exercises
                next = exerciseToLoad.getType();
                learner.setProgress(exerciseToLoad.getExerciseContent()); // set progress to equal what is shown on screen
                Session.incrementAssessmentIndex(); // increment to show we have loaded another exercise
            } else { // if the assessment is done
                next = "/com/example/javafxdemo/assessment_result";
            }
        }
        return next;
    }

    /** for testing only **/
    public void setItems(List<AnagramItem> newItems){
        items = newItems;
    }
}

