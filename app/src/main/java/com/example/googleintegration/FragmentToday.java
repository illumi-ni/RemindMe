package com.example.googleintegration;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class FragmentToday extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef = db.collection("reminder");
    private TaskAdapter adapter;
    View view;

    public FragmentToday(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_today, container, false);

        FloatingActionButton fabNew = view.findViewById(R.id.fabNew);
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddNew.class);
                startActivity(i);
            }
        });
        setUpRecyclerView();

        final SwipeRefreshLayout mSwipe = view.findViewById(R.id.swipe_container);
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
                    adapter.startListening();
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
        String dateFormat = "yyyy-MM-dd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String date = simpleDateFormat.format(new Date());

        Query query = taskRef.whereEqualTo("userId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .whereEqualTo("date", date)
                .orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions.Builder<Task>()
                .setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnTaskListener(new TaskAdapter.OnTaskListener() {
            @Override
            public void onTaskClick(DocumentSnapshot documentSnapshot, int position) {

                Task t = documentSnapshot.toObject(Task.class);
                assert t != null;
                t.setDocumentId(documentSnapshot.getId());
                String documentId = t.getDocumentId();
                String task = documentSnapshot.getString("task");
                String date = documentSnapshot.getString("date");
                String time = documentSnapshot.getString("time");
                String repeat = documentSnapshot.getString("repeat");
                String desc = documentSnapshot.getString("description");

                Intent intent = new Intent(view.getContext(), UpdateTask.class);
                assert task != null;
                intent.putExtra("ID", documentId);
                intent.putExtra("task", task);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("repeat", repeat);
                intent.putExtra("description", desc);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete the task?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.deleteItem(position);
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
                alert.setTitle("Delete task");
                alert.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
