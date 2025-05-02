package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Course;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.javafxdemo.Classes.Learner;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Introduction {
    public Button toTab2, toTab3, toTab4, toTab5;
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab2, tab3, tab4, tab5;

    @FXML
    private Button beginButton;

    @FXML
    private ToggleGroup micToggleGroup;

    public String micPreference;

    @FXML
    private Slider difficultySlider;

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
            } else if (Objects.equals(buttonName, "toTab4")) {
                tabPane.getSelectionModel().select(tab4);
            } else if (Objects.equals(buttonName, "toTab5")) {
                tabPane.getSelectionModel().select(tab5);
            }
    }

    public void saveDifficultyPreference(){
        Session.setDifficultyPreference((int) difficultySlider.getValue()); // Slider always saves val as Double
    }

    public void enableContinue(){
        beginButton.setDisable(false);
    }

    public void saveMicPreference() {
        Session.includeOrExcludeSpeakingExercises(getMicPreference());
    }

    public String getMicPreference() {
        RadioButton selectedRadioButton = (RadioButton) micToggleGroup.getSelectedToggle();
        micPreference = selectedRadioButton.getId();
        return micPreference;
    }

    @FXML
    protected void onStartButtonClick() {
        saveDifficultyPreference();
        saveMicPreference();
        Session.initializeCourse("Italian");

        // Get the current stage
        Stage stage = (Stage) beginButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("content.fxml")));

            Scene newScene = new Scene(root);
            stage.setTitle("Welcome to LangTrans");
            stage.setScene(newScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);       // forcing fullscreen
                stage.centerOnScreen();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getTestScene() {
        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Tab 1");
        Tab tab2 = new Tab("Tab 2");
        tabPane.getTabs().addAll(tab1, tab2);

        // Initially select tab1
        tabPane.getSelectionModel().select(tab1);

        // Return a scene containing the TabPane
        return new Scene(tabPane);
    }
}
