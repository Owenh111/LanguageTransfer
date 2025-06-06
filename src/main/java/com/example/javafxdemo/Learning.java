package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Content;
import com.example.javafxdemo.Classes.Course;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Learning {
    /**
     * I reserved Content.java for the structure of each piece of content read in but this mean that a discrepancy arose
     * between the name of Learning.java and the Content.fxml file it relates to.
     */
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label title, wordType;
    @FXML
    private Label englishConcept, englishExamplePhrase1, englishExamplePhrase2, englishExamplePhrase3;
    @FXML
    private Label italianConcept, italianExamplePhrase1, italianExamplePhrase2, italianExamplePhrase3;
    @FXML
    private TextArea explanation, exceptions;
    @FXML
    private Button continueToExercises, updateSettings, saveAndQuit;

    public void initialize(){
        Session.resetUnusedExercises(); // ensure all exercises are in play

        Session.startColourCycle(anchorPane); // make the background loop round colour scheme
        // this continues (is called) in every class until the end

        // advance learner's progress each time Learning is shown
        Session.advanceProgress();

        Course course = Session.getCurrentCourse();
        List<Content> contentList = course.getContentList(); // everything from the file

        int progressIndex = Session.getLearner().getProgress();
        parseContentAtIndex(progressIndex, contentList); // get the relevant content
    }

    public void parseContentAtIndex(Integer index, List<Content> contentList) {

        Content content = contentList.stream()
                .filter(c -> Objects.equals(c.getContentNumber(), index))
                .findFirst()
                .orElse(null);

        assert content != null;
        populateContent(content);
    }
    public void populateContent(Content content){
        title.setText(content.getEnglishConcept() + " --> " + content.getItalianConcept());
        // e.g. Italian --> Italiano
        wordType.setText("for " + content.getWordType());

        englishConcept.setText(content.getEnglishConcept());
        englishExamplePhrase1.setText(content.getEnglishExamplePhrase());
        englishExamplePhrase2.setText(content.getEnglishExamplePhrase2());
        englishExamplePhrase3.setText(content.getEnglishExamplePhrase3());
        italianConcept.setText(content.getItalianConcept());
        italianExamplePhrase1.setText(content.getItalianExamplePhrase());
        italianExamplePhrase2.setText(content.getItalianExamplePhrase2());
        italianExamplePhrase3.setText(content.getItalianExamplePhrase3());

        explanation.setText(content.getExplanation());
        exceptions.setText(content.getExceptions());
        continueToExercises.setText("Continue to exercises");
    }

    public void updateSettings(){
        Session.decrementProgress(); // so that when progress is auto incremented again, it cancels out
        Session.clearExerciseData(); // so that the old exercise data is not kept and mixed in with the new
        Stage stage = (Stage) continueToExercises.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Introduction.fxml")));
            Parent root = loader.load(); // Usually these are combined but here they are separate so we can get the controller
            // (The root, being a Parent, does not have this functionality)

            // Get controller and jump to settings tab
            Introduction introduction = loader.getController();
            introduction.jumpToSettings();
            introduction.loadDifficultyPreference();

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

    @FXML
    public void onNextButtonClick(){
        Random random = new Random();
        List<String> exercises = Session.getAllExercises(); // this will not get Speaking if it has been excluded
        int index = random.nextInt(exercises.size()); // get a random index from the exercises
        String next = exercises.get(index); // get the exercise that corresponds to this index

        Stage stage = (Stage) continueToExercises.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/javafxdemo/Exercises/"+next+".fxml")));
            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setScene(newScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);       // forcing fullscreen
                stage.centerOnScreen();
            });

            Session.removeUsedExerciseFromRandomSelection(next);
        } catch (IOException e) {
            URL url = getClass().getResource("/com/example/javafxdemo/Exercises/"+next+".fxml");
            System.out.println("URL: " + url);
        }
    }

    public void saveAndQuit(ActionEvent event) {
        Session.save();
        System.exit(0); //quit
    }
}
