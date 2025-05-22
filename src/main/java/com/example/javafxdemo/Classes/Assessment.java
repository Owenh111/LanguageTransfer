package com.example.javafxdemo.Classes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Assessment {
    /**
     * because most of the design of the Assessment consists the Exercises which already exist,
     * this class can remain relatively empty, except for what it needs when first instantiated
     */
    private List<Exercise> assessmentExercises;

    public void initialize(){
        addassessmentExercises();
        Session.enableAssessmentMode();
        Session.saveAssessment(assessmentExercises);
    }

    public Assessment(List<Exercise> assessmentExercises){
        this.assessmentExercises = assessmentExercises;
    }
    public List<Exercise> getAssessmentExercises() {
        return assessmentExercises;
    }

    public void addassessmentExercises(){
        for (Exercise exercise : assessmentExercises){
            String exerciseType = exercise.getType();
            if (Session.getExercisesUnusedInSection().contains(exerciseType)){
                Session.removeUsedExerciseFromRandomSelection(exerciseType);
            }
        }

        while (!Session.getExercisesUnusedInSection().isEmpty()){
            List<String> unusedExercises = Session.getExercisesUnusedInSection();
                Random random = new Random();
                int index = random.nextInt(unusedExercises.size());
                String exerciseType = unusedExercises.get(index);
                int exerciseIndex = (int) ( Math.random() * 2 + 1); // will return either 1 or 2
            // Math.random() will never return 1.0, and since numbers are truncated when going to Int, 3 is impossible
            Exercise exercise = new Exercise(exerciseType, exerciseIndex);
            assessmentExercises.add(exercise);
            Session.removeUsedExerciseFromRandomSelection(exerciseType);
            }
        }
    }
