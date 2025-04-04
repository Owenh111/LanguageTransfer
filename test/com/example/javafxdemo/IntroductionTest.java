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

        // Wait for JavaFX application thread to initialize
        Platform.runLater(() -> {
            Scene scene = introduction.getTestScene();
            stage.setScene(scene);
            stage.show();
        });
    }

    @Test
    void preSelectedTabShouldBeTab1() throws InterruptedException {
        // Launch JavaFX application in a separate thread
        Thread javafxThread = new Thread(() -> {
            try {
                launch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        javafxThread.start();
        javafxThread.join(); // making sure the JavaFX application thread has started
        /** all of the above is necessary to make sure the toolkit has been initialised
         * the above does not interact with the App thread, whereas the below does
         */

        // Once the application is launched, perform the check
        Platform.runLater(() -> {
            Stage testStage = new Stage();
            try {
                start(testStage); // initialises the JavaFX application and the stage
            } catch (Exception e) {
                e.printStackTrace();
            }

            // gets the TabPane from the scene and verify the initial selection
            TabPane tabPane = (TabPane) testStage.getScene().getRoot();
            assertEquals("Tab 1", tabPane.getSelectionModel().getSelectedItem().getText(),
                    "Tab 1 should be selected initially");
        });
    }
}
