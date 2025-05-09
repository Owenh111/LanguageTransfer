package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Course;
import com.example.javafxdemo.Classes.Learner;
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
import java.io.IOException;
import java.util.Objects;

public class Homepage {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label welcomeText;
    @FXML
    private Button resumeButton;

    public void initialize(){
        Session.startColorCycle(anchorPane);
        enableResumeIfSaveDataExists();
    }

    public void setUpNewCourse(){
        Course italian = new Course("Italian"); //create course
        Learner learner = new Learner(italian, 1); //create learner
        // n.b. progress set to 1 as this is the first index and is equal to resetting the course

        learner.setCourse(italian); //set course
        Session.setLearner(learner); //set learner (defers to Session)

        learner.getCourse(); //get course - used here only for debugging. not sure why underlined when below isn't
        Session.getLearner(); //get learner (defers to Session) - used here only for debugging
    }

    public void enableResumeIfSaveDataExists(){
        File saveFile = new File("SaveData.ser");
        if (saveFile.exists()) {
            resumeButton.setDisable(false);
        } else {
            resumeButton.setDisable(true);
        }
    }

    public void loadInFile() {
        Session.load();
        onStartButtonClick();
    }

    @FXML
    protected void onStartButtonClick() {

        welcomeText.setText("Loading");

        setUpNewCourse();
        // Get the current stage
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            /** NOTE: only works when Homepage.java is in this exact package
             * if moving folder may need to change code in module-info.java manually **/
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("introduction.fxml")));

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
            System.out.println(getClass().getResource("introduction.fxml"));
        }
    }
}