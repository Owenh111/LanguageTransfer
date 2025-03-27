package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;

public class Exercise {
    private Character exerciseType; //to load the correct fxml template and exclude microphone tasks
    // 'l' - listening; 'm' - matching; 's' - speaking; 'w' - writing;
    private Integer exerciseContent; //to dynamically create the exercise with content most recently learned

    public Exercise(Character exerciseType, Integer exerciseContent) {
        this.exerciseType = exerciseType;
        this.exerciseContent = exerciseContent;
    }

    public Character getType() {
        return exerciseType;
    }

    public void setType(Character type) {
        this.exerciseType = type;
    }

    public Integer getExerciseContent() {
        return exerciseContent;
    }

    public void setExerciseContent(Integer exerciseContent) {
        this.exerciseContent = exerciseContent;
    }
}
