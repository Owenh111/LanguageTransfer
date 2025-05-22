package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Course;
import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
    private Label welcomeText;
    @FXML
    private Button resumeButton;
    // this class, along with Introduction and AssessmentIntroduction, does not use the coloured background
    // this is because I only set it up to work with AnchorPanes and you cannot swap this out for Pane
    // this works quite well because it accentuates the surprise when you initially get through the Intro
    // and then "wakes the user up" when they get to the Assessment to try and stop them just clicking through
    public void initialize(){
        enableResumeIfSaveDataExists(); // otherwise keep it disabled
        // n.b. I prefer this rather than always enabling the button and then throwing an error if there is no data
    }

    public void setUpNewCourse(){
        Course italian = new Course("Italian"); //create course
        Learner learner = new Learner(italian, 1); //create learner
        // n.b. progress set to 1 as this is the first index and is equal to resetting the course

        learner.setCourse(italian); //set course
        Session.setLearner(learner); //set learner (defers to Session)
    }

    public void enableResumeIfSaveDataExists(){
        File saveFile = new File("SaveData.ser"); // new File does not actually "create" a file
        // this is essentially a pointer to the memory location of a presumed file of this name
        if (saveFile.exists()) { // now checking whether that pointer leads anywhere
            resumeButton.setDisable(false);
        } else {
            resumeButton.setDisable(true);
        }
    }

    @FXML
    public void onResumeButtonClick() {
        welcomeText.setText("Loading"); // this is worth doing as the code below takes a second to compile
        Session.load(); // as the ability to click Resume means there is previous save data, find it
        Course currentCourse = Session.getCurrentCourse(); // finding what we set a minute ago
        currentCourse.loadContentFromFile("content.txt"); // load in everything for the course once
        Session.decrementProgress(); // so that when progress is auto incremented again, it cancels out
        Session.setCourseInitialised();
        Stage stage = (Stage) welcomeText.getScene().getWindow(); // get the current stage using a UI element
        // n.b. you cannot do e.g. Stage stage = (Stage) this.stage();, you have to get it another way
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("content.fxml")));
            // n.b. we are skipping the Introduction above
            // Set the new scene to the stage
            Scene newScene = new Scene(root);
            stage.setTitle("LangTrans Italiano"); // from here onward the window is named differently
            stage.setScene(newScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint(""); // this time we ensure there is no hint (it is on by default)
                stage.setFullScreen(true);       // forcing fullscreen
                stage.centerOnScreen(); // this doesn't actually work! Absolutely no idea why
                // IntelliJ (or Java itself) does seem to be detecting my screen as the wrong size
                // but even in that case I don't know. I kept this in to show I attempted to fix it!
                // the way I got around this was just by maximising/full-screening by default each time
            });
        } catch (IOException e) {
            System.out.println(getClass().getResource("content.fxml"));
            e.printStackTrace();
        }
    }

    @FXML
    protected void onStartButtonClick() {
        welcomeText.setText("Loading");
        setUpNewCourse(); // default method for starting a new course
        // Get the current stage
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
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

    public void onQuitButtonClick(ActionEvent event) {
        System.exit(0); //quit
    }
}