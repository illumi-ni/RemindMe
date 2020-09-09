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

public class ProjectAdapter extends FirestoreRecyclerAdapter<Project, ProjectAdapter.ProjectHolder> {
    private onProjectListener listener;

    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<Project> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProjectAdapter.ProjectHolder holder, int position, @NonNull Project model) {
        holder.txtProjectTitle.setText(model.getProjectTitle());
        holder.txtDateCreated.setText(model.getDateCreated());
    }

    @NonNull
    @Override
    public ProjectAdapter.ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_cardview, parent, false);
        return new ProjectHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ProjectHolder extends RecyclerView.ViewHolder{
        public TextView txtProjectTitle, txtDateCreated;
        public ImageView mDeleteProject;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);
            txtProjectTitle = itemView.findViewById(R.id.textProjectTitle);
            txtDateCreated = itemView.findViewById(R.id.dateCreated);
            mDeleteProject = itemView.findViewById(R.id.deleteProject);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onProjectClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            mDeleteProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    public interface onProjectListener{
        void onProjectClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(int position);
    }

    public void setOnProjectListener(ProjectAdapter.onProjectListener listener){
        this.listener = listener;
    }
}
