package com.example.javafxdemo.Classes;

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
    private static List<Exercise> assessmentData;

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

    public static void setCourseInitialised(){ // this is needed if resume is clicked
        courseInitialised = true; // so that if the user changes their settings it does not reset progress
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

    private static boolean assessmentMode = false;
    private static int assessmentIndex = 0;

    public static void enableAssessmentMode() {
        assessmentMode = true;
    }

    public static void disableAssessmentMode() {
        assessmentMode = false;
    }

    public static boolean inAssessmentMode() {
        return assessmentMode;
    }

    public static void saveAssessment(List<Exercise> assessment) {
        assessmentData = assessment;
    }

    public static List<Exercise> getAssessmentData() {
        return assessmentData;
    }

    public static void incrementAssessmentIndex() {
        assessmentIndex+=1;
    }

    public static int getAssessmentIndex() {
        return assessmentIndex;
    }

    public static void resetAssessmentIndex() { assessmentIndex=0; }

    public static boolean assessmentComplete() {
        if (assessmentIndex == 3){
            return true;
        } else {
            return false;
        }
    }

    private static int skippedAnswers = 0;
    public static void addSkippedAnswer() {
        skippedAnswers += 1;
    }

    public static int getSkippedAnswers() {
        return skippedAnswers;
    }

    public static void resetSkippedAnswers() {
        skippedAnswers = 0;
    }

    /**
     the colours below are used for the coloured background
     i chose them manually as they represent a number of pastel colours which look pleasant
     they also do not clash with the darker tones of the background

     they are ordered in a way which is where the most similar colours are next to each other
     as the code works across the list sequentially to get between each colour
     **/
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

    /** METHODS START HERE **/

    // when the next Learning.java is shown, all Exercises need to be showable again so this is called
    public static void resetUnusedExercises(){
        exerciseTypesUnusedInThisSection = new ArrayList<>(allExercises);
    }

    // the method below is called once at the start or can be called again in the ESC menu
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

    /** Initialise background color cycling for a given AnchorPane
     * The 4 methods below are all part of the same process and are always called one after the other
     * They are commented individually but their overall function is as follows
     * 1: startColorCycle: is called from other classes, and stops any previous cycles to prevent overlap (if you don't it flashes)
     * 2: fadeToNextColor: gets two colours from the list above and shows 100 colours between them to create a fade effect
     * 3:interpolateColor: find the exact Color needed to show at that moment in fadeToNextColor
     * 4: toRgbString: this way the program can actually use the colour using FXML, using a hex code
     *
     * Note: Except for "startColourCycle" and comments I used the american spelling color
     * This is easier than continually switching between the two when coding as e.g. defining a "Colour" won't work
     * @param anchorPane
     */
    public static void startColourCycle(AnchorPane anchorPane) {
        currentAnchorPane = anchorPane;
        if (timeline != null) {
            timeline.stop();
        }
        fadeToNextColor();
    }

    /**        below we get the startColor, colorIndex. colorIndex is not actually reset between calls
     *         this means that every 10 seconds, i.e. every time a colour in colorIndex is "passed through",
     *         the next time that startColourCycle is called it will "go back to" a different colour
     *         this is visible sometimes but makes the process feel smoother
     *         one could try and save the exact interpolatedColor and start from there,
     *         but that would involve manipulating the list of Colors itself and I thought that could cause
     *         more issues because it would be hard-ish to code and the colour would need to be removed after too
     */
    private static void fadeToNextColor() {
        Color startColor = colors[colorIndex];
        Color endColor = colors[(colorIndex + 1) % colors.length]; // the end wraps to the first colour if at the end of the list

        /**
         * you can do (steps / duration) to see how many different interpolated colours are shown per second
         * in this case it is 10. You could lower the number of steps but to compensate you would need fewer seconds
         * the below is a good balance between the two otherwise the fading can be either too quick or unnoticeable
         * also, each KeyFrame added degrades performance (and going higher than screen refresh rate, for example, is pointless)
         */
        final int steps = 100; // defines how smooth fading is
        final Duration duration = Duration.seconds(10); // how long to fade between specified colours

        timeline = new Timeline(); // this handles the animation

        for (int i = 0; i <= steps; i++) {
            double fraction = (double) i / steps; // double suffices because the fractions are only ever hundredths
            Color interpolatedColor = interpolateColor(startColor, endColor, fraction);
            timeline.getKeyFrames().add(new KeyFrame( // defines a new KeyFrame, which is what Timelines are composed of
                    duration.divide(steps).multiply(i), // this line looks complex but is just finding where in the sequence to add it
                    event -> {
                            currentAnchorPane.setStyle("-fx-background-color: " + toRgbString(interpolatedColor));
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
        return new Color(red, green, blue, 1.0); // 1.0 is the opacity
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

    public static String getHint(List<String> data){
        return data.getLast();
    }

    /** the following methods are used for the difficulty settings **/

    private static Pair<List<String>,List<String>> getEasiestPhrases(List<String> data){
        List<String> english = new ArrayList<>();
        List<String> italian = new ArrayList<>();
        List<Integer> validIndices = Arrays.asList(1,2,3); // can be returned as list as we don't need to change the size
        Collections.shuffle(validIndices); // Shuffle to randomize

        for (int i = 0; i < 3; i++) {
            int chosenInt = validIndices.get(i);
            english.add(data.get(chosenInt));
            italian.add(data.get(chosenInt + 3)); // Corresponding Italian phrase is always 3 indices "higher"
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
    } // added for completeness but never used

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
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SaveData.ser"))) { // creates a new file
            out.writeObject(data);
            out.close();
            System.out.println("Session saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("SaveData.ser"))) {
            SaveData data = (SaveData) in.readObject(); // here we read stuff in...

            // ...but below we also need to reset all our variables, because saving only ever happens when quitting
            setLearner(data.getLearner());
            setCurrentCourse(data.getLearner().getCourse());

            setDifficultyPreference(data.getDifficultyPreference());

            micPref = data.getMicPreference();

            improvableExercises.clear();
            improvableExercises.addAll(data.getImprovableExercises());

            System.out.println("Session loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
