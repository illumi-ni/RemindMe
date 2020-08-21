package com.example.googleintegration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskHolder> {
    AlertDialog.Builder builder;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, int position, @NonNull Task model) {
        holder.textViewTask.setText(model.getTask());
        holder.textViewDate.setText(model.getDate());
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        return new TaskHolder(v);
    }

    static class TaskHolder extends RecyclerView.ViewHolder{
        public TextView textViewTask, textViewDate;
        public ImageView mDeleteTask;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textTask);
            textViewDate = itemView.findViewById(R.id.textDate);
            mDeleteTask = itemView.findViewById(R.id.deleteIcon);

//            mDeleteTask.setOnClickListener(new View.OnClickListener() {
//                @Override
////                public void onClick(View view) {
////                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder();
////
////                }
//
//            });
        }

    }
}
