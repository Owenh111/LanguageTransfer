package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Session {
    private static Learner currentLearner;
    private static Course currentCourse;
    private static List<Exercise> improvableExercises = new ArrayList<>();
    private static boolean courseInitialised = false;

    // to return
    static List<String> englishPhrases = new ArrayList<>();
    static List<String> italianPhrases = new ArrayList<>();

    public static void initializeCourse(String language) {
        if (!courseInitialised) { // otherwise progress resets when user changes their settings
            currentCourse = new Course(language);
            currentCourse.loadContentFromFile("content.txt");
            currentLearner.resetCourse();
            courseInitialised = true;
        }
    }

    public static Course getCurrentCourse() {
        return currentCourse;
    }

    public static void setCurrentCourse(Course newCourse) {
        currentCourse = newCourse;
    }

    public static void advanceProgress() { //i.e. change the section
        int progress = currentLearner.getProgress();
        currentLearner.setProgress(progress + 1);
    }

    public static void decrementProgress() { // used when updating settings
        int progress = currentLearner.getProgress();
        currentLearner.setProgress(progress - 1);
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

    private static List<String> allExercises = new ArrayList<>(Arrays.asList(
            "Anagram",
            "Listening",
            "Speaking"
            ,"Translating"
            ));
    private static List<String> exerciseTypesUnusedInThisSection;

    /** call this when user starts or resumes a course */
    public static void setLearner(Learner learner) {
        currentLearner = learner;
    }
    public static Learner getLearner() {
        return currentLearner;
    }

    /** get all Exercises; used in Learning.java as none have been used yet **/
    public static List<String> getAllExercises(){
        return allExercises;
    }

    /** get unused Exercises; called from any exercise as at least one exercise has already been shown **/
    public static List<String> getExercisesUnusedInSection(){
        return exerciseTypesUnusedInThisSection;
    }

    public static int difficultyPreference;
    public static String micPref;

    /* METHODS START HERE */


    /** when the next Learning.java is shown, all Exercises need to be showable again so this is called **/
    public static void resetUnusedExercises(){
        exerciseTypesUnusedInThisSection = new ArrayList<>(allExercises);
    }

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
        micPref = micPreference; // save micPreference to Session for use when saving
    }

    public static void excludeSpeakingExercisesForAssessment(){
        allExercises.remove("Speaking");
    }

    /** Initialise background color cycling for a given AnchorPane */
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

        final int steps = 100; // defines how smooth fading is
        final Duration duration = Duration.seconds(10); // how long to fade between specified colours

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

    private static Color interpolateColor(Color start, Color end, double fraction) { // this means to get from one colour to another
        double red = start.getRed() + (end.getRed() - start.getRed()) * fraction;
        double green = start.getGreen() + (end.getGreen() - start.getGreen()) * fraction;
        double blue = start.getBlue() + (end.getBlue() - start.getBlue()) * fraction;
        return new Color(red, green, blue, 1.0);
    }

    private static String toRgbString(Color color) { // so the program can meaningfully use it
        return String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void clearExerciseData(){
        englishPhrases.clear();
        italianPhrases.clear();
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
        List<Integer> validIndices = Arrays.asList(1,2,3);
        Collections.shuffle(validIndices); // Shuffle to randomize

        for (int i = 0; i < 3; i++) {
            int chosenInt = validIndices.get(i);
            english.add(data.get(chosenInt));
            italian.add(data.get(chosenInt + 3)); // Corresponding Italian phrase
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getEasyPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();

        List<Integer> validIndices = Arrays.asList(1,2,3,7,8,9);
        Collections.shuffle(validIndices); // Shuffle to randomize

        for (int i = 0; i < 3; i++) {
            int chosenInt = validIndices.get(i);
            english.add(data.get(chosenInt));
            italian.add(data.get(chosenInt + 3)); // Corresponding Italian phrase
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getMediumPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        List<Integer> validIndices = Arrays.asList(1,2,3,7,8,9,13,14,15);
        Collections.shuffle(validIndices); // Shuffle to randomize

        for (int i = 0; i < 3; i++) {
            int chosenInt = validIndices.get(i);
            english.add(data.get(chosenInt));
            italian.add(data.get(chosenInt + 3)); // Corresponding Italian phrase
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getHardPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();

        List<Integer> validIndices = Arrays.asList(1,2,3,7,8,9,13,14,15);
        Collections.shuffle(validIndices); // Randomising using number picker can generate the same numbers twice
        // A solution for that was unwieldy so this instead simply adds the numbers for this and shuffles it

        for (int i = 0; i < 3; i++) {
            int chosenInt = validIndices.get(i);
            english.add(data.get(chosenInt));
            italian.add(data.get(chosenInt + 3)); // Corresponding Italian phrase is always 3 indices after the English one
        }
        return new Pair<>(english,italian);
    }

    private static Pair<List<String>,List<String>> getHardestPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        List<Integer> validIndices = Arrays.asList(7,8,9,13,14,15);
        Collections.shuffle(validIndices); // Shuffle to randomize

        for (int i = 0; i < 3; i++) {
            int chosenInt = validIndices.get(i);
            english.add(data.get(chosenInt));
            italian.add(data.get(chosenInt + 3)); // Corresponding Italian phrase
        }
        return new Pair<>(english,italian);
    }

    public static List<String> getEnglishPhrases(){
        return englishPhrases;
    }

    public static List<String> getItalianPhrases(){
        return italianPhrases;
    }

    /** returns data to be loaded in according to difficulty setting **/
    public static Pair<List<String>, List<String>> generateContentForExercise(List<String> data){
        clearExerciseData();
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
        exerciseTypesUnusedInThisSection.remove(exercise);
    }

    public static void save() {
        SaveData data = new SaveData(currentLearner, getDifficultyPreference(), micPref, improvableExercises);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SaveData.ser"))) {
            out.writeObject(data);
            out.close();
            System.out.println("Session saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("SaveData.ser"))) {
            SaveData data = (SaveData) in.readObject();

            setLearner(data.getLearner());
            setCurrentCourse(data.getLearner().getCourse());

            setDifficultyPreference(data.getDifficultyPreference());

            // Restore mic preference
            micPref = data.getMicPreference();

            // Restore improvable exercises
            improvableExercises.clear();
            improvableExercises.addAll(data.getImprovableExercises());

            System.out.println("Session loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
