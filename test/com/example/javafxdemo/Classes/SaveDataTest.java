package com.example.javafxdemo.Classes;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveDataTest {

    @Test
    void testConstructorAndGetters() {
        Course Italian = new Course("Italian");
        Learner learner = new Learner(Italian, 1);
        int difficulty = 2;
        String micPref = "radioButtonYes";
        List<Exercise> exercises = Arrays.asList(new Exercise("Anagram", 1));

        SaveData saveData = new SaveData(learner, difficulty, micPref, exercises);

        assertEquals(learner, saveData.getLearner());
        assertEquals(difficulty, saveData.getDifficultyPreference());
        assertEquals(micPref, saveData.getMicPreference());
        assertEquals(exercises, saveData.getImprovableExercises());
    }

    @Test
    void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        Course Italian = new Course("Italian");
        Learner learner = new Learner(Italian, 1);
        int difficulty = 2;
        String micPref = "enabled";
        List<Exercise> exercises = Arrays.asList(new Exercise("Anagram", 1));

        SaveData original = new SaveData(learner, difficulty, micPref, exercises);

        // Serialize
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.close();

        // Deserialize
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        SaveData deserialized = (SaveData) ois.readObject();

        // below we check EQUALS to check that the serialisation is working
        assertEquals(original.getImprovableExercises().size(), deserialized.getImprovableExercises().size());
        assertEquals(original.getMicPreference(), deserialized.getMicPreference());
        // below we check NOT EQUALS to check they are being stored in different places in memory
        // it is not massively useful but does prove that two different object instances are being accessed
        assertNotEquals(original.getLearner().getCourse(), deserialized.getLearner().getCourse());
    }
}

