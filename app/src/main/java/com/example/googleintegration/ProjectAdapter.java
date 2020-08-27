package com.example.googleintegration;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleintegration.models.ProjectNote;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProjectAdapter extends FirestoreRecyclerAdapter<ProjectList, ProjectAdapter.ProjectHolder> {
    private OnProjectListener listener ;

//    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<ProjectList> options, OnProjectListener OnProjectListener)
    public ProjectAdapter(@NonNull FirestoreRecyclerOptions<ProjectList> options) {
        super(options);
//        this.mOnProjectListener = OnProjectListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProjectHolder holder, int position, @NonNull ProjectList model) {
        holder.listprojectname.setText(model.getProjectname());
        holder.listprojectdesc.setText(model.getProjectdesc());

    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_project_list, parent, false);
        v.setOnClickListener(listener);

//        return new ProjectHolder(v, mOnProjectListener);
        return new ProjectHolder(v);
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    class ProjectHolder extends RecyclerView.ViewHolder{
        public TextView listprojectname;
        public TextView listprojectdesc;
        public ImageView imgDel;
        private final Context context;
//        public OnProjectListener onProjectListener;

//        public ProjectHolder(@NonNull View itemView, final OnProjectListener listener)
        public ProjectHolder(@NonNull View itemView) {
            super(itemView);
            listprojectname = itemView.findViewById(R.id.txtprojectname);
            listprojectdesc = itemView.findViewById(R.id.txtprojectdesc);
 //           this.onProjectListener = onProjectListener;
//            itemView.setOnClickListener(this);
            context = itemView.getContext();
            imgDel = itemView.findViewById(R.id.deleteimg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   final Intent intent = new Intent(v.getContext(), ProjectTask.class);
                   context.startActivity(intent);
                }
            });
            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }


    }
    public interface OnProjectListener extends View.OnClickListener {
        void onProjectClick(View v, int position);
        void onDeleteClick(int position);

    }
    public void setOnProjectListener(OnProjectListener listener){
        this.listener = listener;
    }

}
