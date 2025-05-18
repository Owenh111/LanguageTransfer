package com.example.javafxdemo.Classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void testInitializeCourseInitializesCourseWhenNotInitialized() {
        // Given
        Course language = new Course("English");
        Learner mockLearner = new Learner(language,1);
        Session.setLearner(mockLearner);

        // When
        Session.initializeCourse("English");

        // Then
        Course currentCourse = Session.getCurrentCourse();
        assertNotNull(currentCourse, "Course should be initialized");
        assertEquals(language.toString(), currentCourse.toString(), "Course language should be set correctly");

        mockLearner.resetCourse();
        assertEquals("Course Name: English, Number of Exercises: 0", mockLearner.getCourse().toString(),
                "Learner's course should be reset (ie name still exists but no exercises anymore)");
    }

    @Test
    void testInitializeCourseDoesNotReinitializeWhenAlreadyInitialized() {
        // Given
        Course initialLanguage = new Course("English");
        Learner mockLearner = new Learner(initialLanguage,1);
        Session.setLearner(mockLearner);

        // Initialize once
        Session.initializeCourse("English");

        // Capture the current course instance
        Course initialCourse = Session.getCurrentCourse();

        // When
        String newLanguage = "French";
        Session.initializeCourse(newLanguage);

        // Then
        Course currentCourse = Session.getCurrentCourse();
        assertSame(initialCourse, currentCourse, "Course should not be reinitialized");
        assertEquals(initialLanguage.toString(), currentCourse.toString(), "Course language should remain the same");
    }

    @Test
    void testInitializeCourseMaintainsAlreadySetCourseAsInitialized() {
        // Given
        Course presetCourse = new Course("German");
        Learner mockLearner = new Learner(presetCourse,1);
        mockLearner.setCourse(presetCourse);
        Session.setLearner(mockLearner);
        Session.setCurrentCourse(presetCourse);

        // When
        Session.initializeCourse("Spanish");
        // Then
        assertEquals(presetCourse.toString(), Session.getCurrentCourse().toString(),
                "Preset course should NOT YET be overridden");


        Session.setCurrentCourse(new Course("Spanish"));
        assertNotEquals(presetCourse.toString(), Session.getCurrentCourse().toString(),
                "Preset course SHOULD NOW be overridden");
    }
}