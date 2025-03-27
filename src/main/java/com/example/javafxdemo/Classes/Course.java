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

    void addExercisesToCourse() {

    }

    void addExerciseTypes(){
        /** All exercise types initially added in above function
         * Here the list is iterated through and speaking exercises removed
         */
        Introduction introduction = new Introduction();
        String micPreference = introduction.getMicPreference();

        if (micPreference.equals("radioButtonNo")){
            for (int i = 0; i <= exercises.size(); i++){
                Exercise exercise = exercises.get(i);
                Character exerciseType = exercise.getType();

                if (exerciseType != 's'){
                    exercises.remove(exercise);
                }
            }
        }
    }

    void startCourse(){

    }

    void addAssessment(){

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
