package com.example.javafxdemo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Homepage {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onStartButtonClick() {

        welcomeText.setText("Loading");
        // Get the current stage
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            /** NOTE: only works when Homepage.java is in this exact package
             * if moving folder may need to change code in module-info.java manually **/
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("introduction.fxml")));

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.centerOnScreen();

            //stage can only be this specific size (as tabpane does not dynamically resize)
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setMaxWidth(600);
            stage.setMaxHeight(400);
            stage.setResizable(false);

            stage.setScene(newScene);
        } catch (IOException e) {
            System.out.println(getClass().getResource("introduction.fxml"));
        }
    }
}