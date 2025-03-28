package com.example.javafxdemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homepage.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.centerOnScreen(); //does not actually centre on screen

        /** manual solution below - even this doesn't work properly though **/
        //Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        //stage.setX((screenBounds.getWidth() - stage.getHeight()) / 2);
        //stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

        stage.setTitle("Welcome to LangTrans");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}