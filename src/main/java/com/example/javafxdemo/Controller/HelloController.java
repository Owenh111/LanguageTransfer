package com.example.javafxdemo.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onStartButtonClick() {

        welcomeText.setText("On button click works!");
        try {
            // Load the new FXML file (ie window)
            FXMLLoader loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("introduction.fxml")));
            AnchorPane root = loader.load(); // Load the new scene

            // Get the current stage
            Stage stage = (Stage) welcomeText.getScene().getWindow();

            // Set the new scene to the stage
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}