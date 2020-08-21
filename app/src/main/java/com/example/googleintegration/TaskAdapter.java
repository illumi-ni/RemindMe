package com.example.googleintegration;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskHolder> {
    AlertDialog.Builder builder;
    private OnTaskListener mOnTaskListener;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options, OnTaskListener mOnTaskListener) {
        super(options);
        this.mOnTaskListener = mOnTaskListener;
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
        return new TaskHolder(v, mOnTaskListener);
    }

    public static class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTask, textViewDate;
        public ImageView mDeleteTask;
        OnTaskListener onTaskListener;

        public TaskHolder(@NonNull View itemView, OnTaskListener onTaskListener ) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textTask);
            textViewDate = itemView.findViewById(R.id.textDate);
            mDeleteTask = itemView.findViewById(R.id.deleteIcon);
            this.onTaskListener = onTaskListener;

            itemView.setOnClickListener(this);
//            mDeleteTask.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            onTaskListener.onTaskClick(getAdapterPosition());
        }
    }

    public interface OnTaskListener {
        void onTaskClick(int position);
    }
}





//        private void showDeleteDataDialog(){
//            AlertDialog.Builder builder = new AlertDialog.Builder();
//            builder.setTitle("Delete");
//            builder.setMessage("Are you sure you want to delete the task?")
//                    //set positive/Yes
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                            signOut();
//                            Toast.makeText(getApplicationContext(), "Logged out!",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//        }
