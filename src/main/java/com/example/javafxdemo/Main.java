package com.example.javafxdemo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homepage.fxml")));
        Scene scene = new Scene(root);

        stage.setTitle("Welcome to LangTrans");
        stage.setScene(scene);
        Platform.runLater(() -> {
                    stage.setFullScreenExitHint("");
                    stage.setFullScreen(true);       // forcing fullscreen
                    stage.centerOnScreen();
                });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}