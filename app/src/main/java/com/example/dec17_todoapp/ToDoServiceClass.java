package com.example.dec17_todoapp;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Date;

public class ToDoServiceClass {

    ArrayList<ToDo> todolist = new ArrayList<>(0);

    // context
    void addNewTodo(ToDo newtask){
        todolist.add(newtask);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("task",newtask.task);
//        editor.putString("date",newtask.date);
//        editor.putBoolean("isurgent",newtask.isUrgent);

    }





}
