package com.example.googleintegration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FragmentNotes extends Fragment {
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef = db.collection("note");
    private NoteAdapter noteAdapter;
    View view;

    public FragmentNotes() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        FloatingActionButton fabWrite = view.findViewById(R.id.fabWrite);
        fabWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(view.getContext(), CreateUpdateNote.class);
                startActivity(in);
            }
        });
        setUpRecyclerView();


        final SwipeRefreshLayout mSwipe = view.findViewById(R.id.note_swipe_container);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipe.isRefreshing()) {
                            mSwipe.setRefreshing(false);
                        }
                    }
                }, 1000);
                if(isConnected()) {
                    setUpRecyclerView();
                    noteAdapter.startListening();
                }
                else{
                    Toast.makeText(view.getContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public boolean isConnected() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager)view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    private void setUpRecyclerView(){
        Query query = taskRef.whereEqualTo("userId", (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())).getUid());
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        noteAdapter = new NoteAdapter(options);
        recyclerView = view.findViewById(R.id.noteRecyclerView);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(noteAdapter);

        noteAdapter.setOnNoteListener(new NoteAdapter.OnNoteListener() {
            @Override
            public void onNoteClick(DocumentSnapshot documentSnapshot, int position) {
                Note no = documentSnapshot.toObject(Note.class);
                assert no != null;
                no.setDocumentId(documentSnapshot.getId());
                String noteId = no.getDocumentId();
                String title = documentSnapshot.getString("noteTitle");
                String note = documentSnapshot.getString("noteText");

                Intent in = new Intent(view.getContext(), CreateUpdateNote.class);
                in.putExtra("id", noteId);
                in.putExtra("title", title);
                in.putExtra("note", note);
                startActivity(in);
            }

            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete the note?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                noteAdapter.deleteItem(position);
                                Toast.makeText(view.getContext(), "Deleted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Delete Note");
                alert.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
}
