package com.example.javafxdemo.Exercises;

import com.example.javafxdemo.Classes.Session;
import com.example.javafxdemo.Introduction;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static javafx.application.Application.launch;
import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;

class ListeningTest extends Application {

    @Test
    void testOnCheck_correctAnswerWithoutBonusPhrase() throws InterruptedException {
        Thread javafxThread = new Thread(() -> {
            try {
                launch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        javafxThread.start();
        javafxThread.join(); // making sure the JavaFX application thread has started

        // Once the application is launched, perform the check
        Platform.runLater(() -> {
            Listening listening = new Listening();
            Label feedbackLabel = new Label();
            TextField userInput = new TextField();
            listening.feedbackLabel = feedbackLabel;
            listening.userInput = userInput;

            String correctAnswer = "correct answer";
            Listening.AudioItem mockAudioItem = new Listening.AudioItem(correctAnswer, null, false);
            listening.audioItems.add(mockAudioItem);

            userInput.setText("correct answer");
            listening.onCheck();

            assertEquals("✅ Correct!", feedbackLabel.getText());
            assertTrue(feedbackLabel.getStyle().contains("green"));
        });
    }

    @Test
    void testOnCheck_incorrectAnswerWithoutBonusPhrase() throws InterruptedException {
        Thread javafxThread = new Thread(() -> {
            try {
                launch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        javafxThread.start();
        javafxThread.join(); // making sure the JavaFX application thread has started

        // Once the application is launched, perform the check
        Platform.runLater(() -> {
            Listening listening = new Listening();
            Label feedbackLabel = new Label();
            TextField userInput = new TextField();
            listening.feedbackLabel = feedbackLabel;
            listening.userInput = userInput;

            String correctAnswer = "correct answer";
            Listening.AudioItem mockAudioItem = new Listening.AudioItem(correctAnswer, null, false);
            listening.audioItems.add(mockAudioItem);

            userInput.setText("wrong answer");
            listening.onCheck();

            assertEquals("❌ Try again!", feedbackLabel.getText());
            assertTrue(feedbackLabel.getStyle().contains("red"));
        });
    }

    @Test
    void testOnCheck_incorrectAnswerWithBonusPhrase() throws InterruptedException {
        Thread javafxThread = new Thread(() -> {
            try {
                launch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        javafxThread.start();
        javafxThread.join(); // making sure the JavaFX application thread has started

        // Once the application is launched, perform the check
        Platform.runLater(() -> {
            Listening listening = new Listening();
            Label feedbackLabel = new Label();
            TextField userInput = new TextField();
            listening.feedbackLabel = feedbackLabel;
            listening.userInput = userInput;

            String correctAnswer = "bonus answer";
            Listening.AudioItem mockAudioItem = new Listening.AudioItem(correctAnswer, null, true);
            listening.audioItems.add(mockAudioItem);

            userInput.setText("wrong answer");
            listening.onCheck();

            assertEquals("❌ Try again!", feedbackLabel.getText());
            assertTrue(feedbackLabel.getStyle().contains("red"));
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        Listening listening = new Listening();

        // Wait for JavaFX application thread to initialize
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("listening.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }
}