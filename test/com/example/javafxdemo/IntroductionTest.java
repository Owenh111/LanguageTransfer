package com.example.javafxdemo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntroductionTest extends Application {

    private Introduction introduction;

    @Override
    public void start(Stage stage) throws Exception {
        introduction = new Introduction();

        Scene scene = introduction.getTestScene();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void preSelectedTabShouldBeTab1() {
        Platform.runLater(() -> {
            Stage testStage = new Stage();
            try {
                start(testStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Get the TabPane from the scene and verify the initial selection
            TabPane tabPane = (TabPane) testStage.getScene().getRoot();
            assertEquals("Tab 1", tabPane.getSelectionModel().getSelectedItem().getText(),
                    "Tab 1 should be selected initially");
        });
    }
}
