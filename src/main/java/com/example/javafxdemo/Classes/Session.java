package com.example.javafxdemo.Classes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

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
            //"Anagram",
            "Listening"//, "Speaking", "Translating"
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

    public static int difficultyPreference;

    /** the two methods below are called once at the start or can be called again in the ESC menu **/
    public static void setDifficultyPreference(int preference){
        difficultyPreference = preference;
    }

    public static int getDifficultyPreference(){
        return difficultyPreference;
    }
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

    /** the following is used to load data centrally, instead of in each class, which gives back a list **/
    public static List<String> loadInExerciseDataForSection(int sectionToLoad) {
        List<String> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Session.class.getResourceAsStream
                        ("/com/example/javafxdemo/Exercises/exerciseContent.txt"))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");
                int section = Integer.parseInt(parts[0].trim());
                if (section == sectionToLoad) {
                    if (parts.length == 20) {
                        for (int i = 0; i < parts.length; i++) {
                            data.add(parts[i].trim());
                        }
                        break;
                    } else {
                        throw new RuntimeException("File incorrectly formatted");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /** the following two methods can be called in other classes and get specific data that has been read from a section of the file **/

    public static int getSectionNumber(List<String> data){
        return Integer.parseInt(data.getFirst().trim());
    }

    public static String getHint(List<String> data){
        return data.getLast();
    }

    /** the following methods are used for the difficulty settings **/

    private static Pair<List<String>,List<String>> getEasiestPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            english.add(data.get(i));
        }
        for (int i = 4; i <= 6; i++) {
            italian.add(data.get(i));
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getEasyPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 3; i++) {
            int chosenInt = random.nextInt(1, 7); //1 to 6, as there are 6 easy and medium phrases combined (if confused see below)
            if (chosenInt > 3){ //the easiest way to write this method is to do a nextInt with bounds, but that means...
                chosenInt += 3; //we must add 3 to any numbers not 1-3, as we actually want to pick from indexes 1-3 and 7-9
            }
                english.add(data.get(chosenInt));
                italian.add(data.get(chosenInt+3)); //as each english data piece corresponds to the italian one exactly 3 indexes after
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getMediumPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        for (int i = 7; i <= 9; i++) {
            english.add(data.get(i));
        }
        for (int i = 10; i <= 12; i++) {
            italian.add(data.get(i));
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getHardPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 3; i++) {
            int chosenInt = random.nextInt(7, 13); //1 to 6, which includes all medium and hard phrases (if confused see below)
            if (chosenInt > 9){ //the easiest way to write this method is to do a nextInt with bounds, but that means...
                chosenInt += 3; //we must add 3 to any numbers not 7-9, as we actually want to pick from indexes 7-9 and 13-15
            }
                english.add(data.get(chosenInt));
                italian.add(data.get(chosenInt+3)); //as each english data piece corresponds to the italian one exactly 3 indexes after
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getHardestPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        for (int i = 13; i <= 15; i++) {
            english.add(data.get(i));
        }
        for (int i = 16; i <= 18; i++) {
            italian.add(data.get(i));
        }
        return new Pair<>(english,italian);
    }

    /** returns data to be loaded in according to difficulty setting **/
    public static Pair<List<String>, List<String>> generateContentForExercise(List<String> data){
        // to return
        List<String> englishPhrases = new ArrayList<>();
        List<String> italianPhrases = new ArrayList<>();

        switch (difficultyPreference) {
            case 1: Pair<List<String>,List<String>> easiestPhrases = getEasiestPhrases(data);
                englishPhrases.addAll(easiestPhrases.getKey());
                italianPhrases.addAll(easiestPhrases.getValue());
break;
            case 2: Pair<List<String>,List<String>> easyPhrases = getEasyPhrases(data);
                englishPhrases.addAll(easyPhrases.getKey());
                italianPhrases.addAll(easyPhrases.getValue());
break;
            case 3: Pair<List<String>,List<String>> mediumPhrases = getMediumPhrases(data);
                englishPhrases.addAll(mediumPhrases.getKey());
                italianPhrases.addAll(mediumPhrases.getValue());
break;
            case 4: Pair<List<String>,List<String>> hardPhrases = getHardPhrases(data);
                englishPhrases.addAll(hardPhrases.getKey());
                italianPhrases.addAll(hardPhrases.getValue());
break;
            case 5: Pair<List<String>,List<String>> hardestPhrases = getHardestPhrases(data);
                englishPhrases.addAll(hardestPhrases.getKey());
                italianPhrases.addAll(hardestPhrases.getValue());
                break;
        }
        return new Pair<>(englishPhrases,italianPhrases);
    }

    /** this exercise will not be called again until the unused improvableExercises are reset **/
    public static void removeUsedExerciseFromRandomSelection(String exercise) {
        exercisesUnusedInThisSection.remove(exercise);
    }
}
