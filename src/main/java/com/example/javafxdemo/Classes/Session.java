package com.example.javafxdemo.Classes;

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
    private static Course currentCourse;
    private static List<Exercise> improvableExercises = new ArrayList<>();;

    public static void initializeCourse(String language) {
        currentCourse = new Course(language);
        currentCourse.loadContentFromFile("content.txt");
        currentLearner.resetCourse();
    }

    public static Course getCurrentCourse() {
        return currentCourse;
    }

    public static void advanceProgress() { //i.e. change the section
        int progress = currentLearner.getProgress();
        currentLearner.setProgress(progress + 1);
    }

    public static void addGiveUp(Exercise exercise) {
        improvableExercises.add(exercise);
    }

    public static List<Exercise> getGiveUps(){
        return improvableExercises;
    }

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

    //todo: re-add "Matching" when the exercise has been made
    private static List<String> allExercises = new ArrayList<>(Arrays.asList(
            "Anagram"
            , "Listening", "Speaking", "Translating"
            ));
    private static List<String> exercisesUnusedInThisSection;

    /** call this when user starts or resumes a course */
    public static void setLearner(Learner learner) {
        currentLearner = learner;
    }
    public static Learner getLearner() {
        return currentLearner;
    }

    /** get all improvableExercises; used in Learning.java as none have been used yet **/
    public static List<String> getAllExercises(){
        return allExercises;
    }

    /** get unused improvableExercises; called from any exercise as at least one exercise has already been shown **/
    public static List<String> getExercisesUnusedInSection(){
        return exercisesUnusedInThisSection;
    }

    /** when the next Learning.java is shown, all improvableExercises need to be showable again so this is called **/
    public static void resetUnusedExercises(){
        exercisesUnusedInThisSection = new ArrayList<>(allExercises);
    }

    /** called once at the start **/
    public static void includeOrExcludeSpeakingExercises(String micPreference){
        if (Objects.equals(micPreference, "radioButtonNo")){
            allExercises.remove("Speaking");
        }
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

    /** this exercise will not be called again until the unused improvableExercises are reset **/
    public static void removeUsedExerciseFromRandomSelection(String exercise) {
        exercisesUnusedInThisSection.remove(exercise);
    }
}
