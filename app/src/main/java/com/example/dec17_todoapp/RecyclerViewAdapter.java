package com.example.dec17_todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.ToDoViewHolder>{


    interface RecyclerToDoClickListener {
        void todoSelected(int position);
    }

    ArrayList<ToDo> list;
    Context context;
    RecyclerToDoClickListener listener;

    RecyclerViewAdapter(ArrayList<ToDo> l, Context c){
        list = l;
        context = c;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recycler_row_layout,parent,false);
         return new ToDoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ToDoViewHolder holder,
                                 int position) {
        holder.taskText.setText(list.get(position).task);
        holder.dateText.setText(list.get(position).date);
        if ( list.get(position).isUrgent ) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.lightRed));
        }else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.lightGreen));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder{

        TextView taskText;
        TextView dateText;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            taskText =  itemView.findViewById(R.id.tasktextinrecyclerrow);
            dateText = itemView.findViewById(R.id.datetextinrecyclerrow);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // do nothing here
                    // jsut notify the activity
                    listener.todoSelected(getAdapterPosition());
                }
            });

        }
    }




}
