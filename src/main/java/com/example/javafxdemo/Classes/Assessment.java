package com.example.javafxdemo.Classes;

public class Assessment {
    /**
     * i am currently assuming that the assessments will be dynamic in a similar vein to the exercises
     * depending on the length of the course they could probably be manual as there will not be many
     * alternatively, exercises could be invoked and these could be collated and marked to represent a test
     * in that case an assessment would be defined differently
     */
    private Integer assessmentContent;
    public Assessment(Integer assessmentContent){
        this.assessmentContent = assessmentContent;
    }

    public Integer getAssessmentContent() {
        return assessmentContent;
    }

    public void setAssessmentContent(Integer assessmentContent) {
        this.assessmentContent = assessmentContent;
    }
}
