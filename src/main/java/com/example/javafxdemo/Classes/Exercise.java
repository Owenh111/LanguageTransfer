package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;

public class Exercise {
    private String exerciseType; //to load the correct fxml template and exclude microphone tasks
    private Integer exerciseContent; //to dynamically populate the exercise with content most recently learned

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
