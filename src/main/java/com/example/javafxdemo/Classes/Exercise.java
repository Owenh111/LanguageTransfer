package com.example.javafxdemo.Classes;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String exerciseType; //to load the correct fxml template and exclude microphone tasks

    private Integer exerciseContent; //to dynamically create the exercise with content most recently learned

    public Exercise(String exerciseType, Integer exerciseContent) {
        this.exerciseType = exerciseType;
        this.exerciseContent = exerciseContent;
    }

    public String getType() {
        return exerciseType;
    }

    public void setType(String type) {
        this.exerciseType = type;
    }

    public Integer getExerciseContent() {
        return exerciseContent;
    }

    public void setExerciseContent(Integer exerciseContent) {
        this.exerciseContent = exerciseContent;
    }
}
