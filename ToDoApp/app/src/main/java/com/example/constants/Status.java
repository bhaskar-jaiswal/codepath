package com.example.constants;

/**
 * Created by bhaskarjaiswal on 6/9/16.
 */
public enum Status {
    TODO("TODO"), DONE("DONE");
    private String value;

    private Status(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
};
