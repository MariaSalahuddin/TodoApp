package com.example.dec17_todoapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerToDoList extends AppCompatActivity implements
    RecyclerViewAdapter.RecyclerToDoClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_to_do_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_list);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(
                ((MyAPP)getApplication()).myservice.todolist, this  );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter.listener = this;
    }

    @Override
    public void todoSelected(int position) {
        String t= ((MyAPP)getApplication()).myservice.todolist.get(position).task;
        Toast.makeText(this,t,Toast.LENGTH_LONG).show();
    }
}