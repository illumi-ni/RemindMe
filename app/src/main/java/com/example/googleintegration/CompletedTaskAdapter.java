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

public class CompletedTaskAdapter extends FirestoreRecyclerAdapter<CompletedTask, CompletedTaskAdapter.CompletedTaskHolder> {
    private OnCompletedTaskListener listener;

    public CompletedTaskAdapter(@NonNull FirestoreRecyclerOptions<CompletedTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompletedTaskAdapter.CompletedTaskHolder holder, int position, @NonNull CompletedTask model) {
        holder.textViewTask.setText(model.getTask());
        holder.textViewDate.setText(model.getDate());
        holder.textViewTime.setText(model.getTime());
    }

    @NonNull
    @Override
    public CompletedTaskAdapter.CompletedTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        return new CompletedTaskHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class CompletedTaskHolder extends RecyclerView.ViewHolder{
        public TextView textViewTask, textViewDate, textViewTime;
        public ImageView mDeleteTask;

        public CompletedTaskHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textTask);
            textViewDate = itemView.findViewById(R.id.textDate);
            textViewTime = itemView.findViewById(R.id.textTime);
            mDeleteTask = itemView.findViewById(R.id.deleteIcon);

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

    public interface OnCompletedTaskListener{
        void onDeleteClick(int position);
    }

    public void setOnCompletedTaskListener(OnCompletedTaskListener listener) {
        this.listener = listener;
    }
}
