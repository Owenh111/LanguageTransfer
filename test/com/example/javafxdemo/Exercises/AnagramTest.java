package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Course;
import com.example.javafxdemo.Classes.Learner;
import com.example.javafxdemo.Classes.Session;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class AnagramTest {

    /** The following test fails and I was unable to fix it so that it works.
     * The instantiation never occurs properly but I do not understand why not.
     * In my other tests I have instead changed the variables to be package-private...
     * ...to get around this issue. In this case I decided to leave it as I ran out of time...
     * ...and spent enough time trying to make it work that I thought it was best to raise in the report.
     */
    @Test
    void testOnCheck_CorrectAnswer() throws Exception {
        if (!Platform.isFxApplicationThread()) {
            final CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            latch.await();
        }
        // Set learner session
        Learner learner = new Learner(new Course("Italian"), 1);
        Session.setLearner(learner);

        // Load FXML and get controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Anagram.fxml"));
        Parent root = loader.load();
        Anagram anagram = loader.getController();

        // Inject mock UI components (or rely on FXML if UI is loaded)
        anagram.userInput.setText("ciao");
        anagram.setItems(List.of(new Anagram.AnagramItem("ciao")));
        anagram.currentIndex = 0;
        anagram.feedbackLabel = new Label();
        anagram.phrasesCompleted = 0;

        // Act
        Platform.runLater(() -> anagram.onCheck());

        // Wait for FX thread to process
        Thread.sleep(200); // whoever designed JUnit testing is wild for making me do this manually
    }
}