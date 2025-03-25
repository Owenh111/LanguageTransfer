package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;

import java.util.ArrayList;

public class Course {
    private ArrayList<Exercise> exercises;
    private Learner learner;

    void enableOrDisableSpeakingExercises(){
        Introduction introduction = new Introduction();
        String micPreference = introduction.getMicPreference();

        if (micPreference.equals("radioButtonNo")){
            for (int i = 0; i <= exercises.size(); i++){
                Exercise exercise = exercises.get(i);
                // get Exercise type
            }
        }
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }
}
