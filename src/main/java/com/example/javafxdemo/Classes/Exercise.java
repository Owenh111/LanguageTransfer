package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Exercise {
    private Character exerciseType; //to load the correct fxml template and exclude microphone tasks
    // 'l' - listening; 'm' - matching; 's' - speaking; 'w' - writing;
    private Integer exerciseContent; //to dynamically create the exercise with content most recently learned
    private Boolean showMicExercises = true;
    private List<Character> exercises = Arrays.asList('l','m','r','w');
    public Exercise(Character exerciseType, Integer exerciseContent) {
        this.exerciseType = exerciseType;
        this.exerciseContent = exerciseContent;
    }

    public void loadExercise(){

    }

    public void includeOrExcludeSpeakingExercises(){
        Introduction introduction = new Introduction();
        String micPreference = introduction.getMicPreference();

        if (Objects.equals(micPreference, "RadioButtonNo")){
            exercises.remove('s');
        }
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
