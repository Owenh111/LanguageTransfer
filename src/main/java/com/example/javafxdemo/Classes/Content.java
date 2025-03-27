package com.example.javafxdemo.Classes;

public class Content {
    /**
     * this class Content exists to connect assessmentContent, exerciseContent and progress
     * exerciseContent will have many small pieces of content
     * assessmentContent combines multiple pieces of exerciseContent to occur in a single assessment
     * progress can be measured as a % of the total integer value of exerciseContent+assessmentContent
     * getter and setter methods in the other classes can therefore defer to this
     */

    public Content(){

    }
}
