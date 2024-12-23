package com.example.dec17_todoapp;


import android.app.AlarmManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity
        extends AppCompatActivity implements
        ToDoBaseAdapter.SwitchListener, ToDoBaseAdapter.AlarmListener, ToDoBaseAdapter.DeleteClickListener
, ToDoBaseAdapter.EmailListener {

    ToDoBaseAdapter adapter;
    ListView todolist;
    FloatingActionButton addbutton;
    FloatingActionButton torecyclerButton;
    private AlarmManager alarmManager;
    ToDoServiceClass serviceClass;
    ArrayList<ToDo> list;
    private ActivityResultLauncher<Intent> myLauncher;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        serviceClass = ((MyAPP) getApplication()).myservice;
        todolist = findViewById(R.id.tasklist);
        addbutton = findViewById(R.id.addnewtask);
        torecyclerButton = findViewById(R.id.torecyclerlist);
        sharedPref = this.getSharedPreferences("alltodos", Context.MODE_PRIVATE);
        // MODE_PRIVATE is overwriting the exsisting file
        //MODE_APPEND is continue writing -- only works with wrting to a file
        readFromSharedPreferences();

        adapter = new ToDoBaseAdapter(serviceClass.todolist, this);
        todolist.setAdapter(adapter);
        adapter.switchListener = this;
        adapter.deleteClickListener = this;
        adapter.emailListener = this;
        adapter.alarmListener = this;

        myLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                ToDo d = (ToDo) data.getSerializableExtra("newtodo");
                ((MyAPP) getApplication()).myservice.todolist.add(d);
                updateTheListInStorage();

                adapter.notifyDataSetChanged();
            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addnewtaskIntent = new Intent(MainActivity.this, AddToDo.class);
                // startActivity(addnewtaskIntent);
                myLauncher.launch(addnewtaskIntent);
            }
        });

        torecyclerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RecyclerToDoList.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void switchChanged(int taskIndex, Boolean value) {
        ((MyAPP) getApplication()).myservice.todolist.get(taskIndex).isUrgent = value;
        updateTheListInStorage();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteClicked(int taskIndex) {
        // add an alert
        new AlertDialog.Builder(this).setTitle("Delete!!!").
                setMessage("Are You Sure You Want To Delete this Task?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // on delete
                        ((MyAPP) getApplication()).myservice.todolist.remove(taskIndex);
                        updateTheListInStorage();
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("No", null).show();
    }

    private boolean isExactAlarmPermissionGranted() {
        return getApplicationContext().checkSelfPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED;
    }

    void updateTheListInStorage() {
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();

        // convert list to string(JSON)
        // convert string(JSON) to list
        //encoding
        String json = gson.toJson(((MyAPP) getApplication()).myservice.todolist);
        editor.putString("alltasks", json);
        editor.apply();
    }

    void readFromSharedPreferences() {
        // read one todo at a time (string, string, boolean)
        String jsonfromPreferences = sharedPref.getString("alltasks", "");
        Gson gson = new Gson();
        // decoding
        list = gson.fromJson(jsonfromPreferences, new TypeToken<ArrayList<ToDo>>() {
        }.getType());
        if (list == null) {
            ((MyAPP) getApplication()).myservice.todolist = new ArrayList<>(0);
        } else {
            ((MyAPP) getApplication()).myservice.todolist = list;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.mainactivity_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_todo_menu) {
            Intent addnewtaskIntent = new Intent(MainActivity.this, AddToDo.class);
            myLauncher.launch(addnewtaskIntent);
        } else if (item.getItemId() == R.id.torecycler_menu) {
            Intent i = new Intent(MainActivity.this, RecyclerToDoList.class);
            startActivity(i);
        }

        return true;
    }


    @Override
    public void alarmClicked(int taskIndex) {
        String date = list.get(taskIndex).date; // Get the date from your task list (in "yyyy/MM/dd" format)
        try {
            // Parse the date string to a Date object
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            Date convertedDate = format.parse(date);

            if (convertedDate != null) {
                // Create a Calendar object and set the time using the parsed date
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(convertedDate);

                // Ensure alarm time is in the future (if the time is in the past)
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1); // If the alarm time is in the past, set for the next day
                }
                // Create the intent to set the alarm
                Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, calendar.get(Calendar.HOUR_OF_DAY));  // Set the hour
                alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, calendar.get(Calendar.MINUTE));  // Set the minutes
                alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, list.get(taskIndex).task);  // Set task description as message

                // Start the alarm activity (this opens the default alarm clock app for user confirmation)
                if (alarmIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(alarmIntent);
                    Toast.makeText(this, "Alarm set for " + calendar.getTime(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No alarm app found on this device!", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error setting alarm", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendEmail(int taskIndex) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));  // Ensure only email apps are shown
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"}); // Recipient email address
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Todo Reminder"); // Subject of the email
        emailIntent.putExtra(Intent.EXTRA_TEXT, "You have to do task: "+ list.get(taskIndex).task+" due on "+ list.get(taskIndex).date ); // Email body
        try {
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}