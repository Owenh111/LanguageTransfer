package com.example.javafxdemo.Classes;

public class Learner {
    private Course course; //to tie the learner to a specific course
    private int progress; //can load back in to where the user was when they close the program

    public Learner(Course course, Integer progress) {
        this.course = course;
        this.progress = progress;
    }
    public void resetCourse(){
        setProgress(1); // the first index with exercises is 1 as this acts like a section
        // so my design choice was to set the first as if it were Section 1 rather than Section 0
        // this does mean de-iterating the integer when displaying progress to the user (not yet done)
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
