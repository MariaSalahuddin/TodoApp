package com.example.dec17_todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ToDoBaseAdapter extends BaseAdapter {

    interface SwitchListener {
        void switchChanged(int taskIndex,Boolean value);
    }
    interface AlarmListener {
        void alarmClicked(int taskIndex);
    }
    interface DeleteClickListener {
        void deleteClicked(int taskIndex);
    }
    interface EmailListener {
        void sendEmail(int taskIndex);
    }
    SwitchListener switchListener;
    EmailListener emailListener;
    DeleteClickListener deleteClickListener;
    AlarmListener alarmListener;
    // todo array list
    // I need the row layout --> context == run-time memory that contians the app resources
    ArrayList<ToDo> list;
    Context context;

    ToDoBaseAdapter(ArrayList<ToDo> l, Context c ){
        list = l;
        context = c;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //LayoutInflater takes the layout id and give me the view object for it.
       // LayoutInflater.from(context).
        View v = LayoutInflater.from(context).inflate(R.layout.row_layout,viewGroup,false);

        TextView taskText = v.findViewById(R.id.tasktextinrow);
        TextView dateText = v.findViewById(R.id.datetextinrow);
        Switch s = v.findViewById(R.id.switchinrow);
        ImageButton deleteButton = v.findViewById(R.id.deletebutton);
        ImageButton emailButton = v.findViewById(R.id.emailButton);
        ImageButton alarmButton = v.findViewById(R.id.alarmButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClickListener.deleteClicked(i);
            }
        });
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailListener.sendEmail(i);
            }
        });

        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmListener.alarmClicked(i);
            }
        });

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("in list adapter", "switch changed");
                // notify the main activity that there is an update.
                switchListener.switchChanged(i, b);
            }
        });

        taskText.setText(list.get(i).task);
        dateText.setText(list.get(i).date);
        s.setChecked(list.get(i).isUrgent);
       if ( list.get(i).isUrgent ) {
           v.setBackgroundColor(context.getResources().getColor(R.color.lightRed));
           deleteButton.setBackgroundColor(context.getResources().getColor(R.color.lightRed));
           emailButton.setBackgroundColor(context.getResources().getColor(R.color.lightRed));
           alarmButton.setBackgroundColor(context.getResources().getColor(R.color.lightRed));
       }else {
           v.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
           deleteButton.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
           emailButton.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
           alarmButton.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
       }
        return v;
    }


}
