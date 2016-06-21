package com.example.constants;

/**
 * Created by bhaskarjaiswal on 6/9/16.
 */
public enum Priority {
    LOW("LOW"), MEDIUM("MEDIUM"), HIGH("HIGH");
    private String value;

    private Priority(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
};
