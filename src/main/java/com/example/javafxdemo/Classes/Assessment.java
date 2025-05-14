package com.example.javafxdemo.Classes;

import java.util.List;
import java.util.Random;

public class Assessment {
    private List<Exercise> assessmentContent;

    public void initialize(){
        addAssessmentContent();
    }

    public Assessment(List<Exercise> assessmentContent){
        this.assessmentContent = assessmentContent;
    }
    public List<Exercise> getAssessmentContent() {
        return assessmentContent;
    }
    public void setAssessmentContent(List<Exercise> assessmentContent) {
        this.assessmentContent = assessmentContent;
    }

    public void addAssessmentContent(){
        for (Exercise exercise : assessmentContent){
            String exerciseType = exercise.getType();
            if (Session.getExercisesUnusedInSection().contains(exerciseType)){
                Session.removeUsedExerciseFromRandomSelection(exerciseType);
            }
        }

        while (!Session.getExercisesUnusedInSection().isEmpty()){
            List<String> unusedExercises = Session.getExercisesUnusedInSection();
            String next = "";
                Random random = new Random();
                int index = random.nextInt(unusedExercises.size());
                next = unusedExercises.get(index);
            }
        }
    }
