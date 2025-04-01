package com.example.javafxdemo;

import com.example.javafxdemo.Classes.Content;
import com.example.javafxdemo.Classes.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class Learning {
    /**
     * The related FXML currently acts as a placeholder and by extension so does this.
     * I'm thinking there should be a general layout for content which is dynamically updated at each stage.
     * Maybe a couple different versions of it to keep things fresh and accommodate for changes.
     * This will be shown between each exercise. Exercise content could be read in from a file(?)
     *
     * Similarly, I'm thinking each type of exercise should have its own "content" with a standard layout.
     * Each FXML part can then be populated dynamically with the specific content that has just been learned.
     * The type of exercise would be randomly chosen after each piece of teaching, maybe 2 or 3 in a row.
     * Using my current layout this would presumably open in a new window each time - maybe not ideal.
     */
    @FXML
    private Label title, wordType;
    @FXML
    private Label englishConcept, englishExamplePhrase1, englishExamplePhrase2, englishExamplePhrase3;
    @FXML
    private Label italianConcept, italianExamplePhrase1, italianExamplePhrase2, italianExamplePhrase3;
    @FXML
    private TextArea explanation, exceptions;
    @FXML
    private Button continueToExercises;

    public void populateContent(Integer contentNumber){
        Content content = new Content(contentNumber,"","","",
                "","","","",
                "","","","");

        //Course course = new Course("Italian");
        //List<Content> contentList = course.getContentOnCourse();
        title.setText(content.getEnglishConcept() + " --> " + content.getItalianConcept());
    }
}
