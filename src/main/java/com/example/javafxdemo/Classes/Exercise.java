package com.example.javafxdemo.Classes;

import java.io.Serializable;

public class Exercise implements Serializable { // implements Serializable so it can be written when saving
    private String exerciseType; //to load the correct fxml template and exclude microphone tasks
    private Integer exerciseContent; //to dynamically create the exercise with content most recently learned

    public Exercise(String exerciseType, Integer exerciseContent) {
        this.exerciseType = exerciseType;
        this.exerciseContent = exerciseContent;
    }

    public String getType() {
        return exerciseType;
    }
    public Integer getExerciseContent() {
        return exerciseContent;
    }
}
