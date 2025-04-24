package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Translating {

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
    private Label feedbackLabel;

    Learner learner = Session.getLearner();

    Integer sectionToLoad = learner.getProgress();
    ArrayList<String> answers = new ArrayList<>();

    public void initialize() {
        readInData();
        setUpLabels();
    }
    public void readInData(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/example/javafxdemo/content.txt"))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
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
    public void checkAnswer(String answer, Integer answerIndex) {
            //todo: see github commit
        if (true) { //todo: change this too
            feedbackLabel.setText("✔️ Correct");
            feedbackLabel.setStyle("-fx-text-fill: green;");
        } else {
            feedbackLabel.setText("❌ Incorrect");
            feedbackLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public void onCheck1(ActionEvent event) {
        String answerFieldContent = answerField1.getText();

        if (!answerFieldContent.isEmpty()){
            checkAnswer(answerFieldContent, 7);
        }
    }

    public void onGiveUp1(ActionEvent event) {
    }

    public void onCheck2(ActionEvent event) {
        String answerFieldContent = answerField2.getText();

        if (!answerFieldContent.isEmpty()){
            checkAnswer(answerFieldContent, 8);
        }
    }

    public void onGiveUp2(ActionEvent event) {
    }

    public void onCheck3(ActionEvent event) {
        String answerFieldContent = answerField3.getText();

        if (!answerFieldContent.isEmpty()){
            checkAnswer(answerFieldContent, 9);
        }
    }

    public void onGiveUp3(ActionEvent event) {
    }
}
