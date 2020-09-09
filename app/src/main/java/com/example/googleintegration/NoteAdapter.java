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

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {
    private OnNoteListener listener;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteAdapter.NoteHolder holder, int position, @NonNull Note model) {
        holder.txtViewTitle.setText(model.getNoteTitle());
        holder.txtViewNote.setText(model.getNoteText());
        holder.txtDateModified.setText(model.getDateModified());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_cardview, parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        public TextView txtViewTitle, txtViewNote, txtDateModified;
        public ImageView mDeleteNote;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            txtViewTitle = itemView.findViewById(R.id.textNoteTitle);
            txtViewNote = itemView.findViewById(R.id.textNoteText);
            txtDateModified = itemView.findViewById(R.id.dateModified);
            mDeleteNote = itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onNoteClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            mDeleteNote.setOnClickListener(new View.OnClickListener() {
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

    public interface OnNoteListener {
        void onNoteClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(int position);
    }

    public void setOnNoteListener(NoteAdapter.OnNoteListener listener){
        this.listener = listener;
    }
}
