package com.example.googleintegration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskHolder> {
    private OnTaskListener listener;

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskHolder holder, final int position, @NonNull final Task model) {
        holder.textViewTask.setText(model.getTask());
        holder.textViewDate.setText(model.getDate());
        holder.textViewTime.setText(model.getTime());
        holder.textViewRepeat.setText(model.getRepeat());
        holder.textViewDesc.setText(model.getDesc());
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        return new TaskHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class TaskHolder extends RecyclerView.ViewHolder{
        public TextView textViewTask, textViewDate, textViewTime, textViewRepeat, textViewDesc;
        public ImageView mDeleteTask;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textTask);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewTime = itemView.findViewById(R.id.textTime);
            textViewRepeat = itemView.findViewById(R.id.textRepeat);
            textViewDesc = itemView.findViewById(R.id.textDesc);
            mDeleteTask = itemView.findViewById(R.id.deleteIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onTaskClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            mDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnTaskListener {
        void onTaskClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(int position);
    }

    public void setOnTaskListener(OnTaskListener listener){
        this.listener = listener;
    }
}

