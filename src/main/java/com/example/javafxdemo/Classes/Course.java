package com.example.javafxdemo.Classes;

import com.example.javafxdemo.Introduction;
import com.example.javafxdemo.Learning;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Course implements Serializable {
    private String courseName;
    private ArrayList<Exercise> exercises;
    private List<Content> contentList = new ArrayList<>();

    public Course (String courseName){
        this.courseName = courseName;
        exercises = new ArrayList<Exercise>();
    }

    public List<Content> getContentList(){
        return contentList;
    }
    // Method to read content from the file and create Content objects
    public void loadContentFromFile(String filename) {
        InputStream inputStream = getClass().getResourceAsStream("/com/example/javafxdemo/content.txt");
        Scanner scanner = new Scanner(inputStream);

        // Read through each line in the file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split("`");

            // Ensure the line has the correct number of fields (12 because of wordType)
            if (fields.length == 13) {
                // Create a new Content object from the fields
                Content content = new Content(
                        Integer.parseInt(fields[0]),       // contentNumber
                        fields[1],                         // englishConcept
                        fields[2],                         // italianConcept
                        fields[3],                         // wordType
                        fields[4],                         // englishExamplePhrase
                        fields[5],                         // englishExamplePhrase2
                        fields[6],                         // englishExamplePhrase3
                        fields[7],                         // italianExamplePhrase
                        fields[8],                         // italianExamplePhrase2
                        fields[9],                         // italianExamplePhrase3
                        fields[10],                        // explanation
                        fields[11],                        // exceptions
                        fields[12]                         // hint
                );
                contentList.add(content); // Add the created content to the list
            } else {
                System.out.println("Error adding content - does it have the correct number of fields?");
            }
        }

        scanner.close();
    }
}
