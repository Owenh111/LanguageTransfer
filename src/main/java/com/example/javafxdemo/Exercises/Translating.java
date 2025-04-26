package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    // Feedback label
    @FXML
    private Label feedbackLabel1, feedbackLabel2, feedbackLabel3;

    @FXML
    private Button accentedAButton, accentedEButton, accentedIButton, accentedOButton, accentedUButton;

    @FXML
    private Button continueButton;

    Learner learner = Session.getLearner();

    Integer sectionToLoad = learner.getProgress();
    ArrayList<String> answers = new ArrayList<>();

    Boolean answer1Correct = false;
    Boolean answer2Correct = false;
    Boolean answer3Correct = false;

    public void initialize() {
        Session.startColorCycle(anchorPane);
        readInData();
        setUpLabels();
        enableAccentMarkButtonsIfNecessary(answers);
    }

    public void readInData(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/javafxdemo/content.txt"))))) {
            String line;
            String[] parts;
            while ((line = reader.readLine()) != null) {
                parts = line.split("`");
                if (parts.length >= 11) {
                    int section = Integer.parseInt(parts[0].trim());
                    if (section == sectionToLoad) {
                        answers.add(parts[4].trim());
                        answers.add(parts[5].trim());
                        answers.add(parts[6].trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpLabels(){
        question1.setText(answers.get(0));
        question2.setText(answers.get(1));
        question3.setText(answers.get(2));
    }

    public void enableAccentMarkButtonsIfNecessary(ArrayList<String> answers){
        boolean hasGraveA = false, hasGraveE = false, hasGraveI = false, hasGraveO = false, hasGraveU = false;

        for (String item : answers) {
            if (item.contains("à")) hasGraveA = true;
            if (item.contains("è")) hasGraveE = true;
            if (item.contains("ì")) hasGraveI = true;
            if (item.contains("ò")) hasGraveO = true;
            if (item.contains("ù")) hasGraveU = true;
        }

        accentedAButton.setVisible(hasGraveA);
        accentedEButton.setVisible(hasGraveE);
        accentedIButton.setVisible(hasGraveI);
        accentedOButton.setVisible(hasGraveO);
        accentedUButton.setVisible(hasGraveU);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
