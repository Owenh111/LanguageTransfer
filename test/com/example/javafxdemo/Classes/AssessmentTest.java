package com.example.javafxdemo.Classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentTest {

    private List<Exercise> initialExercises;

    @BeforeEach
    void setUp() {
        // Reset the Session before each test
        // Session.clear(); // This method should reset all static variables if not already available

        // Mock unused exercise types
        Session.resetUnusedExercises();
        Session.enableAssessmentMode();

        initialExercises = new ArrayList<>();
//        initialExercises.add(new Exercise("Anagram", 1)); // Used once already
//
//        // Exercise is now "used"
//        Session.removeUsedExerciseFromRandomSelection("Anagram");
    }

    @Test
    void testAddAssessmentExercisesIncludingSpeaking() {
        Assessment assessment = new Assessment(initialExercises);
        assessment.addassessmentExercises();

        List<Exercise> allExercises = assessment.getAssessmentExercises();

        // Should now include all 3 types at least once
        Set<String> foundTypes = new HashSet<>();
        for (Exercise e : allExercises) {
            foundTypes.add(e.getType());
        }

        assertTrue(foundTypes.contains("Anagram"));
        assertTrue(foundTypes.contains("Listening"));
        assertTrue(foundTypes.contains("Translating"));

        assertEquals(4, allExercises.size());
    }

//    @Test
//    void testInitializeSetsAssessmentModeAndSavesSession() {
//        Assessment assessment = new Assessment(initialExercises);
//        assessment.initialize();
//
//        assertTrue(Session.inAssessmentMode());
//
//        List<Exercise> sessionSaved = Session.getSavedAssessment();
//        assertNotNull(sessionSaved);
//        assertFalse(sessionSaved.isEmpty());
//    }
}
