package com.example.javafxdemo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    /**
     * This class introduces the code used to set the stage and shows the first class, the Homepage.
     * @param stage
     * @throws Exception
     *
     * GENERAL NOTE ABOUT COMMENTING: comments have been generally created and checked according to
     * the order the classes are seen in the program. Therefore, if something has already been commented
     * and appears in a later class it may not be commented there.
     *
     * Many methods in Exercises are commented across all as they can be shown in any order, but others
     * are only commented in one or two. Some code where the function is obvious is not explicitly commented.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homepage.fxml")));
        Scene scene = new Scene(root); // the Scene is what you are putting onto the Stage

        stage.setTitle("Welcome to LangTrans"); // now only seen if full screen is exited
        stage.setScene(scene); // the stage is what holds the scene and can only show 1 at any time
        Platform.runLater(() -> {
                    stage.setFullScreenExitHint("Full screen can be exited with Escape"); //only once
                    stage.setFullScreen(true);       // forcing fullscreen but user can change this
                });

        stage.show(); // the stage does not show by default
    }

    public static void main(String[] args) {
        launch(args);
    }
}