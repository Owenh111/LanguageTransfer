package com.example.javafxdemo.Classes;

import java.io.Serializable;
import java.util.List;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Learner learner;
    private int difficultyPreference;
    private String micPreference;
    private List<Exercise> improvableExercises;

    public SaveData(Learner learner, int difficultyPreference, String micPreference, List<Exercise> improvableExercises) {
        this.learner = learner;
        this.difficultyPreference = difficultyPreference;
        this.micPreference = micPreference;
        this.improvableExercises = improvableExercises;
    }

    public Learner getLearner() {
        return this.learner;
    }

    public int getDifficultyPreference() {
        return this.difficultyPreference;
    }

    public String getMicPreference(){
        return this.micPreference;
    }

    public List<Exercise> getImprovableExercises(){
        return this.improvableExercises;
    }
}