package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Assessment;
import com.example.javafxdemo.Classes.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

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

    public void beginTest(){
        Session.excludeSpeakingExercisesForAssessment();
        Session.resetUnusedExercises();
        // TODO: 14/05/2025 remember to add the speaking back after if the following:
        // the user replays the whole section AND their micPreference is radioButtonYes

        Assessment assessment = new Assessment(Session.getGiveUps());
        assessment.initialize();
    }
}
