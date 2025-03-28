package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Content;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Introduction {
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab2, tab3, tab4, tab5;

    @FXML
    private Button beginButton;

    @FXML
    private ToggleGroup micToggleGroup;

    public String micPreference;

    private List<Content> contentList = new ArrayList<>();

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

    public void enableContinue(){
        beginButton.setDisable(false);
    }

    public void saveMicPreference() {
        RadioButton selectedRadioButton = (RadioButton) micToggleGroup.getSelectedToggle();

        micPreference = selectedRadioButton.getId();
    }

    public String getMicPreference() {
        return micPreference;
    }

    public void loadInModules() {
        loadContentFromFile("content.txt");

        // Print all the content for now (You can use them later in your UI)
        for (Content content : contentList) {
            System.out.println("Content Number: " + content.getContentNumber());
            System.out.println("Word Type: " + content.getWordType());
            System.out.println("English Concept: " + content.getEnglishConcept());
            System.out.println("Italian Concept: " + content.getItalianConcept());
            // Currently only printing these fields to test reading is working correctly
        }

        // Setup of JavaFX UI components will need to occur somewhere
    }

    // Method to read content from the file and create Content objects
    private void loadContentFromFile(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/com/example/javafxdemo/content.txt");
        Scanner scanner = new Scanner(inputStream);

        // Read through each line in the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split(",");

            // Ensure the line has the correct number of fields (12 because of wordType)
            if (fields.length == 12) {
                // Create a new Content object from the fields
                Content content = new Content(
                        Integer.parseInt(fields[0]),       // contentNumber
                        fields[1],                         // wordType
                        fields[2],                         // englishConcept
                        fields[3],                         // italianConcept
                        fields[4],                         // englishExamplePhrase
                        fields[5],                         // englishExamplePhrase2
                        fields[6],                         // englishExamplePhrase3
                        fields[7],                         // italianExamplePhrase
                        fields[8],                         // italianExamplePhrase2
                        fields[9],                         // italianExamplePhrase3
                        fields[10],                        // explanation
                        fields[11]                         // exceptions
                );
                contentList.add(content); // Add the created content to the list
            }
        }

        scanner.close();
    }

    @FXML
    protected void onStartButtonClick() {
        saveMicPreference();
        loadContentFromFile("content.txt");
        loadInModules();
        // Get the current stage
        Stage stage = (Stage) beginButton.getScene().getWindow();
        try {
            // Load the new FXML file (ie window)
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("content.fxml")));

            // Set the new scene to the stage
            Scene newScene = new Scene(root);

            stage.setScene(newScene);

            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("Langtrans Italiano");
            stage.centerOnScreen();
        } catch (IOException e) {

        }
    }

    public Scene getTestScene() {
        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Tab 1");
        Tab tab2 = new Tab("Tab 2");
        tabPane.getTabs().addAll(tab1, tab2);

        // Initially select tab1
        tabPane.getSelectionModel().select(tab1);

        // Return a scene containing the TabPane
        return new Scene(tabPane);
    }
}
