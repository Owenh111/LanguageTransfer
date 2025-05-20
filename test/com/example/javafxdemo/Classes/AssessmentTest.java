package com.example.javafxdemo.Classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentTest {

    private List<Exercise> initialExercises;

    @BeforeEach
    void setUp() {

        // mocking unused exercise types
        Session.resetUnusedExercises();
        Session.enableAssessmentMode();

        initialExercises = new ArrayList<>();
//        initialExercises.add(new Exercise("Anagram", 1));
//        Session.removeUsedExerciseFromRandomSelection("Anagram");
    }

    @Test
    void testAddAssessmentExercisesAndExcludeSpeaking() {
        Assessment assessment = new Assessment(initialExercises);
        assessment.addassessmentExercises();

        List<Exercise> allExercises = assessment.getAssessmentExercises();

        // Should now include all 4 types at least once
        Set<String> foundTypes = new HashSet<>();
        for (Exercise e : allExercises) {
            if (e.getType() != "Speaking"){
                foundTypes.add(e.getType());
            }
        }

        assertTrue(foundTypes.contains("Anagram"));
        assertTrue(foundTypes.contains("Listening"));
        assertTrue(foundTypes.contains("Translating"));

        assertEquals(3, foundTypes.size());
    }
}
