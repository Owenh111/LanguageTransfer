package com.example.javafxdemo.Classes;

public class Learner {
    private Course course;
    private int progress;
    void resetCourse(){
        course = new Course();
    }

    public Course getCourse() {
        return course;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
