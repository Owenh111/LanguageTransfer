package com.example.javafxdemo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IntroductionTest {
    void onStart(Stage stage){
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();
        tabPane.getTabs().addAll(tab1,tab2);
        tabPane.getSelectionModel().select(tab1);

        Button btnToNextTab = new Button();
        btnToNextTab.setOnAction(actionEvent ->
                tabPane.getSelectionModel().select(tab2));

        stage.setScene(new Scene(tabPane));
        stage.show();
    }

    @Test
    void preSelectedTabShouldBeTab1(){
        Stage testStage = new Stage();
        onStart(testStage);

        TabPane tabPane = (TabPane) testStage.getScene().getRoot();

        // Assert that the initially selected tab is tab1
        assertEquals("Tab 1", tabPane.getSelectionModel().getSelectedItem().getText(), "Tab 1 should be selected initially");
    }

    @Test
    void preSelectedTabShouldBeTab1CallsFromIntroduction(){
        Introduction introduction = new Introduction();


        // Using Platform.runLater to run the JavaFX code on the correct thread
        Platform.runLater(() -> {
            Stage testStage = new Stage();
            TabPane tabPane = (TabPane) testStage.getScene().getRoot();

            // Assert that the initially selected tab is tab1
            assertEquals("Tab 1", tabPane.getSelectionModel().getSelectedItem().getText(), "Tab 1 should be selected initially");
        });
    }
    @Test
    void goToNextTab() {
        Stage testStage = new Stage();
        onStart(testStage);

        TabPane tabPane = (TabPane) testStage.getScene().getRoot();
        Button button = (Button) testStage.getScene().lookup(".button");
        button.fire();

        // Assert that the initially selected tab is tab1
        assertEquals("Tab 2", tabPane.getSelectionModel().getSelectedItem().getText(), "Tab 2 should be selected now");
    }
}