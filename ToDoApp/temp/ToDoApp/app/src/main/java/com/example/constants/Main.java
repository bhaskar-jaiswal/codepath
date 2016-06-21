package com.example.constants;

import java.util.Date;

/**
 * Created by bhaskarjaiswal on 6/9/16.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(new Date());

        for (Status status : Status.values()) {
            System.out.println(status +" " + status.getValue());
        }

        for (Priority priority : Priority.values()) {
            System.out.println((priority.getValue() instanceof String) +" " + priority.getValue());
        }
    }
}
