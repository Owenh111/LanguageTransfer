package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Introduction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Session {
    private static Learner currentLearner;

    private static final Color[] colors = {
            Color.valueOf("97ECF1"),
            Color.valueOf("DFFDFF"),
            Color.valueOf("BDB2FF"),
            Color.valueOf("FAD1FA"),
            Color.valueOf("FEC868"),
            Color.valueOf("F1F7B5")
    };
    private static int colorIndex = 0;
    private static Timeline timeline;
    private static AnchorPane currentAnchorPane;
    private static List<String> exercises = new ArrayList<>(Arrays.asList(
            "Listening", "Matching", "Speaking", "Translating"));
    private static List<String> exercisesUsedInThisSection;

    /** call this when user starts or resumes a course */
    public static void setLearner(Learner learner) {
        currentLearner = learner;
    }
    public static Learner getLearner() {
        return currentLearner;
    }

    public static List<String> getAllExercises(){
        return exercises;
    }

    public static List<String> trackExercisesUsedInSection(String exercise){
        exercisesUsedInThisSection.add(exercise);
        return exercisesUsedInThisSection;
    }
    public static List<String> includeOrExcludeSpeakingExercises(String micPreference){
        if (Objects.equals(micPreference, "radioButtonNo")){
            exercises.remove("Speaking");
        }
        return exercises;
    }

    /** Initialize background color cycling for a given AnchorPane */
    public static void startColorCycle(AnchorPane anchorPane) {
        currentAnchorPane = anchorPane;
        if (timeline != null) {
            timeline.stop();
        }
        fadeToNextColor();
    }

    private static void fadeToNextColor() {
        Color startColor = colors[colorIndex];
        Color endColor = colors[(colorIndex + 1) % colors.length];

        final int steps = 100; // Number of steps for smooth fading
        final Duration duration = Duration.seconds(10); // Total duration of the fade

        timeline = new Timeline();

        for (int i = 0; i <= steps; i++) {
            double fraction = (double) i / steps;
            Color interpolatedColor = interpolateColor(startColor, endColor, fraction);
            timeline.getKeyFrames().add(new KeyFrame(
                    duration.divide(steps).multiply(i),
                    event -> {
                        if (currentAnchorPane != null) {
                            currentAnchorPane.setStyle("-fx-background-color: " + toRgbString(interpolatedColor));
                        }
                    }
            ));
        }

        timeline.setOnFinished(event -> {
            colorIndex = (colorIndex + 1) % colors.length;
            fadeToNextColor(); // Proceed to next color
        });

        timeline.play();
    }

    private static Color interpolateColor(Color start, Color end, double fraction) {
        double red = start.getRed() + (end.getRed() - start.getRed()) * fraction;
        double green = start.getGreen() + (end.getGreen() - start.getGreen()) * fraction;
        double blue = start.getBlue() + (end.getBlue() - start.getBlue()) * fraction;
        return new Color(red, green, blue, 1.0);
    }

    private static String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
