package com.example.datasource;

import com.example.constants.Priority;
import com.example.constants.Status;

import java.io.Serializable;

/**
 * Created by bhaskarjaiswal on 6/9/16.
 */
public class Tasks implements Serializable {
    private long id;
    private String taskName;
    private String notes;
    private String dueDate;
    private Priority priority;
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
