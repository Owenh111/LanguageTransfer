package com.example.javafxdemo.Classes;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Content {
    /**
     * this class Content exists to connect assessmentContent, exerciseContent and progress
     * exerciseContent will have many small pieces of content
     * assessmentContent combines multiple pieces of exerciseContent to occur in a single assessment
     * progress can be measured as a % of the total integer value of exerciseContent+assessmentContent
     * getter and setter methods in the other classes can therefore defer to this
     */
    private Integer contentNumber;
    private String wordType;
    private String englishConcept;
    private String italianConcept;
    private String englishExamplePhrase;
    private String englishExamplePhrase2;
    private String englishExamplePhrase3;
    private String italianExamplePhrase;
    private String italianExamplePhrase2;
    private String italianExamplePhrase3;
    private String explanation;
    private String exceptions;

    public Content(Integer contentNumber, String wordType, String englishConcept, String italianConcept, String englishExamplePhrase,
                   String englishExamplePhrase2, String englishExamplePhrase3, String italianExamplePhrase,
                   String italianExamplePhrase2, String italianExamplePhrase3, String explanation, String exceptions){
        this.contentNumber = contentNumber;
        this.wordType = wordType;
        this.englishConcept = englishConcept;
        this.italianConcept = italianConcept;
        this.englishExamplePhrase = englishExamplePhrase;
        this.englishExamplePhrase2 = englishExamplePhrase2;
        this.englishExamplePhrase3 = englishExamplePhrase3;
        this.italianExamplePhrase = italianExamplePhrase;
        this.italianExamplePhrase2 = italianExamplePhrase2;
        this.italianExamplePhrase3 = italianExamplePhrase3;
        this.explanation = explanation;
        this.exceptions = exceptions;
    }

    public Integer getContentNumber() { return contentNumber; }
    public void setContentNumber(Integer contentNumber) { this.contentNumber = contentNumber; }

    public String getWordType() { return wordType; }
    public void setWordType(String wordType) { this.wordType = wordType; }

    public String getEnglishConcept() { return englishConcept; }
    public void setEnglishConcept(String englishConcept) { this.englishConcept = englishConcept; }

    public String getItalianConcept() { return italianConcept; }
    public void setItalianConcept(String italianConcept) { this.italianConcept = italianConcept; }

    public String getEnglishExamplePhrase() { return englishExamplePhrase; }
    public void setEnglishExamplePhrase(String englishExamplePhrase) { this.englishExamplePhrase = englishExamplePhrase; }

    public String getEnglishExamplePhrase2() { return englishExamplePhrase2; }
    public void setEnglishExamplePhrase2(String englishExamplePhrase2) { this.englishExamplePhrase2 = englishExamplePhrase2; }

    public String getEnglishExamplePhrase3() { return englishExamplePhrase3; }
    public void setEnglishExamplePhrase3(String englishExamplePhrase3) { this.englishExamplePhrase3 = englishExamplePhrase3; }

    public String getItalianExamplePhrase() { return italianExamplePhrase; }
    public void setItalianExamplePhrase(String italianExamplePhrase) { this.italianExamplePhrase = italianExamplePhrase; }

    public String getItalianExamplePhrase2() { return italianExamplePhrase2; }
    public void setItalianExamplePhrase2(String italianExamplePhrase2) { this.italianExamplePhrase2 = italianExamplePhrase2; }

    public String getItalianExamplePhrase3() { return italianExamplePhrase3; }
    public void setItalianExamplePhrase3(String italianExamplePhrase3) { this.italianExamplePhrase3 = italianExamplePhrase3; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getExceptions() { return exceptions; }
    public void setExceptions(String exceptions) { this.exceptions = exceptions; }
}
