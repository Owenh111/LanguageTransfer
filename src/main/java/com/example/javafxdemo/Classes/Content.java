package com.example.javafxdemo.Classes;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Content implements Serializable { // implements Serializable so it can be written when saving
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
    private String hint;

    public Content(Integer contentNumber, String englishConcept, String italianConcept, String wordType, String englishExamplePhrase,
                   String englishExamplePhrase2, String englishExamplePhrase3, String italianExamplePhrase,
                   String italianExamplePhrase2, String italianExamplePhrase3, String explanation, String exceptions, String hint){
        this.contentNumber = contentNumber;
        this.englishConcept = englishConcept;
        this.italianConcept = italianConcept;
        this.wordType = wordType;
        this.englishExamplePhrase = englishExamplePhrase;
        this.englishExamplePhrase2 = englishExamplePhrase2;
        this.englishExamplePhrase3 = englishExamplePhrase3;
        this.italianExamplePhrase = italianExamplePhrase;
        this.italianExamplePhrase2 = italianExamplePhrase2;
        this.italianExamplePhrase3 = italianExamplePhrase3;
        this.explanation = explanation;
        this.exceptions = exceptions;
        this.hint = hint;
    }

    // below is a list of getters, but there are no setters as we do not need to set the content in the file

    public Integer getContentNumber() { return contentNumber; }

    public String getWordType() { return wordType; }

    public String getEnglishConcept() { return englishConcept; }

    public String getItalianConcept() { return italianConcept; }

    public String getEnglishExamplePhrase() { return englishExamplePhrase; }
    public String getEnglishExamplePhrase2() { return englishExamplePhrase2; }
    public String getEnglishExamplePhrase3() { return englishExamplePhrase3; }
    public String getItalianExamplePhrase() { return italianExamplePhrase; }
    public String getItalianExamplePhrase2() { return italianExamplePhrase2; }
    public String getItalianExamplePhrase3() { return italianExamplePhrase3; }
    public String getExplanation() { return explanation; }
    public String getExceptions() { return exceptions; }

    // getHint is not set as it does not need to be shown when showing content and so went unused
    // it is saved in Session and gotten from there instead when needed in subsequent classes
    // the reason Session does not need it from here is because it is always the last thing on any line
    // so we can just use getLast()
}
