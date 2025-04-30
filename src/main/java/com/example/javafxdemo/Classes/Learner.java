package com.example.javafxdemo.Classes;

public class Learner {
    private Course course; //to tie the learner to a specific course
    private int progress; //can load back in to where the user was when they close the program

    public Learner(Course course, Integer progress) {
        this.course = course;
        this.progress = progress;
    }
    public void resetCourse(){
        setProgress(0); // as initialize in Learning increments every time it is called anyway
        // this may mean progress needs to be de-incremented when implementing resume/save+load
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) { this.course = course; }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
