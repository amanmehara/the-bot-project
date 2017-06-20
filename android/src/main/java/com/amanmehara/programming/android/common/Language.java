package com.amanmehara.programming.android.common;

/**
 * Created by @amanmehara on 20-06-2017.
 */
public enum Language {

    C("C"),
    CPP("C++"),
    CSHARP("C#"),
    FSHARP("F#"),
    GROOVY("Groovy"),
    JAVA("Java"),
    JAVASCRIPT("JavaScript"),
    PHP("PHP"),
    PYTHON("Python"),
    SCALA("Scala"),
    SQL("SQL");

    private String display;

    Language(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

}
