package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Course;
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
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab2, tab3, tab4, tab5;

    @FXML
    private Button beginButton;

    @FXML
    private ToggleGroup micToggleGroup;

    public String micPreference;

    private Learner learner;

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

    public void enableContinue(){
        beginButton.setDisable(false);
    }

    public void saveMicPreference() {
        RadioButton selectedRadioButton = (RadioButton) micToggleGroup.getSelectedToggle();

        micPreference = selectedRadioButton.getId();
    }

    public String getMicPreference() {
        return micPreference;
    }

    @FXML
    protected void onStartButtonClick() {
        saveMicPreference();
        // Get the current stage
        Stage stage = (Stage) beginButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("content.fxml")));

            //Learning learning = new Learning();
            // Set the new scene to the stage
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.setMaximized(true); // change to setFullScreen at the very end
            // it looks better but right now gets in the way of debugging
            stage.setTitle("Langtrans Italiano");
            stage.initOwner(new Stage());                // makes it owned → not shown in taskbar
            stage.centerOnScreen();
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
