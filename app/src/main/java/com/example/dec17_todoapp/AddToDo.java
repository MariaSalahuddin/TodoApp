package com.example.dec17_todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddToDo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_to_do);
        Button save = findViewById(R.id.saveButton);
        Button cancel = findViewById(R.id.cancelButton);
        EditText taskText = findViewById(R.id.taskText);
        SwitchCompat isurgent = findViewById(R.id.isUrgentSwitch);
        DatePicker taskDatePicker = findViewById(R.id.taskDatePicker);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!taskText.getText().toString().isEmpty()){
                    String taskDate = taskDatePicker.getYear()+"/"+(taskDatePicker.getMonth() + 1)+"/"+taskDatePicker.getDayOfMonth();

                    ToDo newTodo = new ToDo(taskText.getText().toString(),taskDate,isurgent.isChecked());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newtodo",newTodo);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });


    }
}