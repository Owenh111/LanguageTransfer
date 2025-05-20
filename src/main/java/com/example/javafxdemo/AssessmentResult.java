package com.example.javafxdemo;

import com.example.javafxdemo.Classes.SaveData;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

public class AssessmentResult {
    @FXML private AnchorPane anchorPane;
    @FXML private Label outcome, score, redoAssessmentLocked, goToHomepageLocked;
    @FXML private Button goToHomepageButton, redoCourseButton, redoAssessmentButton;

    public void initialize(){
        int skippedAnswers = Session.getSkippedAnswers();
        setupUI(skippedAnswers);
        Session.resetSkippedAnswers();
        Session.resetUnusedExercises();
        Session.resetAssessmentIndex();
        Session.startColorCycle(anchorPane);
    }

    public void setupUI(int skippedAnswers){
        switch (skippedAnswers) {
            case 0:
                outcome.setText("Flawless!");
                break;
            case 1:
                outcome.setText("Nearly perfect!");
                break;
            case 2:
                outcome.setText("Good job!");
                break;
            case 3:
                outcome.setText("You passed.");
                break;
            case 4,5:
                outcome.setText("Close but no cigar.");
                goToHomepageLocked.setVisible(true);
                goToHomepageButton.setDisable(true);
                redoAssessmentButton.setDisable(false);
                redoCourseButton.setDisable(false);
                break;
            default:
                outcome.setText("You have not passed.");
                goToHomepageLocked.setVisible(true);
                redoAssessmentLocked.setVisible(true);
                goToHomepageButton.setDisable(true);
                redoCourseButton.setDisable(false);
                break;
        }
        score.setText("You skipped " + Session.getSkippedAnswers() + " times.");
    }

    public void redoAssessment(){
        Stage stage = (Stage) redoCourseButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("assessment_introduction.fxml")));
            Parent root = loader.load(); // Usually these are combined but here they are separate so we can get the controller
            // (The root, being a Parent, does not have this functionality)

            // Get controller
            AssessmentIntroduction introduction = loader.getController();
            introduction.jumpToSettings();

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setTitle("Welcome to LangTrans");
            stage.setScene(newScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);       // forcing fullscreen
                stage.centerOnScreen();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void redoCourse(){
        Session.decrementProgress(); // since there are only two sections, we don't need to set manually or reset
        Stage stage = (Stage) redoCourseButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("content.fxml")));
            Parent root = loader.load();

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setTitle("Welcome to LangTrans");
            stage.setScene(newScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);       // forcing fullscreen
                stage.centerOnScreen();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void finish(){
        File file = new File("SaveData.ser");

        if (file.exists()) {
            file.delete(); // now all progress is complete save data will be out of date
            // and since the course has finished there is no point overwriting. So, we delete it
        }

        Stage stage = (Stage) redoCourseButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("homepage.fxml")));
            Parent root = loader.load();

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setTitle("Welcome to LangTrans");
            stage.setScene(newScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);       // forcing fullscreen
                stage.centerOnScreen();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
