package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Assessment;
import com.example.javafxdemo.Classes.Exercise;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AssessmentIntroduction {
    @FXML
    public Button toTab2, toTab3;
    @FXML
    TabPane tabPane;
    @FXML
    Tab tab2,tab3;
    @FXML
    private Button beginButton;

    @FXML
    public void goToNextTab(ActionEvent event) {
        // Get the source of the action event (button clicked)
        Button sourceButton = (Button) event.getSource();
        String buttonName = sourceButton.getId();
        // Check which button was clicked and show the next tab accordingly
        if (Objects.equals(buttonName, "toTab2")) {
            tabPane.getSelectionModel().select(tab2);
        } else if (Objects.equals(buttonName, "toTab3")) {
            tabPane.getSelectionModel().select(tab3);
        }
    }

    public void jumpToSettings() { // used when user chooses to update settings
        Platform.runLater(() -> tabPane.getSelectionModel().select(tab3));
    }

    @FXML
    public void beginTest(){
        Session.excludeSpeakingExercisesForAssessment();
        Session.resetUnusedExercises();
        // TODO: 14/05/2025 remember to add the speaking back after if the following:
        // the user replays the whole section AND their micPreference is radioButtonYes

        Assessment assessment = new Assessment(Session.getGiveUps());
        assessment.initialize();

        List<Exercise> assessmentExercises = assessment.getAssessmentExercises();
        Stage stage = (Stage) beginButton.getScene().getWindow();
        Exercise exercise = assessmentExercises.getFirst();
            try {
                Session.getLearner().setProgress(exercise.getExerciseContent());
                String next = exercise.getType();
                // Load the new FXML file (ie window)
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource
                        ( "/com/example/javafxdemo/Exercises/"+next+".fxml")));

                Session.incrementAssessmentIndex();
                // Set the new scene to the stage
                Scene newScene = new Scene(root);

                stage.setScene(newScene);

                stage.setMaximized(true);
                stage.setFullScreen(true);
                stage.setResizable(true);
                stage.setTitle("Langtrans Italiano");
                stage.centerOnScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
