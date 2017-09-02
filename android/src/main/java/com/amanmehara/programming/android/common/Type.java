package com.amanmehara.programming.android.common;

/**
 * Created by @amanmehara on 29-06-2017.
 */
public enum Type {

    FILE("file"),
    DIRECTORY("dir");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
