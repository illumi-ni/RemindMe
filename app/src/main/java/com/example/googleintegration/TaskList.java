package com.example.googleintegration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class TaskList extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef = db.collection("remainder");

    private TaskAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Task List");

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = taskRef.orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
