package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Classes.Learner;

/** do i need this?**/
public class Session {
    private static Learner currentLearner;

    /** call this when user starts or resumes a course */
    public static void setLearner(Learner learner) {
        currentLearner = learner;
    }
    public static Learner getLearner() {
        return currentLearner;
    }

    public Integer returnNumberOfTimesGiveUpPressed() {
        return 0; // TODO: 25/04/2025 so that user ability can be calculated
    }
}
