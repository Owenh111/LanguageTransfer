package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Content;
import com.example.javafxdemo.Classes.Course;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    private Color[] colors = {Color.valueOf("97ECF1"), Color.valueOf("DFFDFF"), Color.valueOf("BDB2FF"),
            Color.valueOf("FAD1FA"), Color.valueOf("FEC868"), Color.valueOf("F1F7B5")};
    private int colorIndex = 0;

    public void initialize(){
        Course course = new Course("Italian");
        course.loadContentFromFile("content.txt");

        cycleColors();

        List<Content> contentList = course.getContentList();
        parseContentAtIndex(1, contentList);
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
        continueToExercises.setText("Continue to exercise " + (content.getContentNumber() + 1));
    }

    @FXML
    public void cycleColors() {
        fadeToNextColor();
    }

    private void fadeToNextColor() {
        Color startColor = colors[colorIndex];
        Color endColor = colors[(colorIndex + 1) % colors.length];

        final int steps = 100; // Number of steps for smooth fading
        final Duration duration = Duration.seconds(10); // Total duration of the fade

        Timeline timeline = new Timeline();

        for (int i = 0; i <= steps; i++) {
            double fraction = (double) i / steps;
            Color interpolatedColor = interpolateColor(startColor, endColor, fraction);
            timeline.getKeyFrames().add(new KeyFrame(
                    duration.divide(steps).multiply(i),
                    event -> anchorPane.setStyle("-fx-background-color: " + toRgbString(interpolatedColor))
            ));
        }

        timeline.setOnFinished(event -> {
            colorIndex = (colorIndex + 1) % colors.length;
            fadeToNextColor(); // Proceed to the next color after fading
        });

        timeline.play();
    }

    private Color interpolateColor(Color start, Color end, double fraction) {
        double red = start.getRed() + (end.getRed() - start.getRed()) * fraction;
        double green = start.getGreen() + (end.getGreen() - start.getGreen()) * fraction;
        double blue = start.getBlue() + (end.getBlue() - start.getBlue()) * fraction;
        return new Color(red, green, blue, 1.0);
    }

    public String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public void onNextButtonClick(){
        // Get the current stage
        Stage stage = (Stage) continueToExercises.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/javafxdemo/listening.fxml")));

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setScene(newScene);

            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("Langtrans Italiano");
            stage.centerOnScreen();
        } catch (IOException e) {

        }
    }
}
