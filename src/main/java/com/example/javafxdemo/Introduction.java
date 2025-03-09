package com.example.javafxdemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Objects;

public class Introduction {
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab2, tab3, tab4, tab5;

    @FXML
    public void goToNextTab(ActionEvent event) {
        // Get the source of the action event (button clicked)
        Button sourceButton = (Button) event.getSource();
        String buttonName = sourceButton.getId();
            // Check which button was clicked and show the next tab accordingly
            if (Objects.equals(buttonName, "toTab2")) {
                tabPane.getSelectionModel().select(tab2);
            } else if (Objects.equals(buttonName, "toTab3")) {
                tabPane.getSelectionModel().select(tab3);
            } else if (Objects.equals(buttonName, "toTab4")) {
                tabPane.getSelectionModel().select(tab4);
            } else if (Objects.equals(buttonName, "toTab5")) {
                tabPane.getSelectionModel().select(tab5);
            }
    }
}
