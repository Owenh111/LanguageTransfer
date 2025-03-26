package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private ArrayList<Exercise> exercises;

    public Course (String courseName){
        this.courseName = courseName;
        exercises = new ArrayList<Exercise>();
    }
    void addExerciseTypes(){
        /** a couple methods to only include speaking exercises in the rotation if the user has a mic:
         * 1) add all exercise types initially in one function then remove speaking ones if RButton checked,
         * or 2) only add exercises to course after RButton part and exclude speaking ones if necessary
         */
        Introduction introduction = new Introduction();
        String micPreference = introduction.getMicPreference();

        if (micPreference.equals("radioButtonNo")){
            for (int i = 0; i <= exercises.size(); i++){
                Exercise exercise = exercises.get(i);
                String exerciseType = exercise.getType();

                if (exerciseType != "speaking"){

                }
            }
        }
    }

    void addExercisesToCourse() {

    }

    void startCourse(){

    }
}
