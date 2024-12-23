package com.example.dec17_todoapp;

import java.io.Serializable;
import java.util.Date;

public class ToDo implements Serializable {

    String task;
    String date;
    Boolean isUrgent;

    public ToDo(String task, String date, Boolean isUrgent) {
        this.task = task;
        this.date = date;
        this.isUrgent = isUrgent;
    }

}
