package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Content;
import com.example.javafxdemo.Classes.Course;
import com.example.javafxdemo.Classes.Session;
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
     * The related FXML currently acts as a placeholder and by extension so does this.
     * I'm thinking there should be a general layout for content which is dynamically updated at each stage.
     * Maybe a couple different versions of it to keep things fresh and accommodate for changes.
     * This will be shown between each exercise. Exercise content could be read in from a file(?)
     *
     * Similarly, I'm thinking each type of exercise should have its own "content" with a standard layout.
     * Each FXML part can then be populated dynamically with the specific content that has just been learned.
     * The type of exercise would be randomly chosen after each piece of teaching, maybe 2 or 3 in a row.
     * Using my current layout this would presumably open in a new window each time - maybe not ideal.
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
    private Button continueToExercises;

    public void initialize(){
        Session.resetUnusedExercises();

        // Only start color cycle (visual)
        Session.startColorCycle(anchorPane);

        // Advance learner's progress each time Learning is shown
        Session.advanceProgress();

        Course course = Session.getCurrentCourse();
        List<Content> contentList = course.getContentList();

        int progressIndex = Session.getLearner().getProgress();
        parseContentAtIndex(progressIndex, contentList);
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

    @FXML
    public void onNextButtonClick(){
        Random random = new Random();
        List<String> exercises = Session.getAllExercises();
        int index = random.nextInt(exercises.size());
        String nextExercise = exercises.get(index);
        // Get the current stage
        Stage stage = (Stage) continueToExercises.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Exercises/"+nextExercise+".fxml")));

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setScene(newScene);

            stage.setMaximized(true);
            stage.setTitle("Langtrans Italiano");
            stage.centerOnScreen();

            Session.removeUsedExerciseFromRandomSelection(nextExercise);
        } catch (IOException e) {
            URL url = getClass().getResource("/com/example/javafxdemo/Exercises/"+nextExercise+".fxml");
            System.out.println("URL: " + url);
        }
    }
}
