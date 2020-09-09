package com.example.googleintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateProject extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnCreate;
    AlertDialog.Builder builder;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference taskRef = db.collection("project");
    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        setTitle("Projects");

        setUpRecyclerView();

        final SwipeRefreshLayout mSwipe = findViewById(R.id.project_swipe_container);
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
                    projectAdapter.startListening();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(CreateProject.this);
                final EditText projectName = new EditText(CreateProject.this);

                builder.setTitle("New Project").setMessage("Name this new project").setView(projectName);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String project = projectName.getText().toString();
                        saveProject(project);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    public boolean isConnected() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    private void setUpRecyclerView() {
        Query query = taskRef.whereEqualTo("userId", (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())).getUid());
        FirestoreRecyclerOptions<Project> options = new FirestoreRecyclerOptions.Builder<Project>()
                .setQuery(query, Project.class)
                .build();

        projectAdapter = new ProjectAdapter(options);
        recyclerView = findViewById(R.id.projectRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(projectAdapter);

        projectAdapter.setOnProjectListener(new ProjectAdapter.onProjectListener() {
            @Override
            public void onProjectClick(DocumentSnapshot documentSnapshot, int position) {
                Project no = documentSnapshot.toObject(Project.class);
                assert no != null;
                no.setDocumentId(documentSnapshot.getId());
                String projectId = no.getDocumentId();
                String projectTitle = documentSnapshot.getString("projectTitle");
                String dateCreated = documentSnapshot.getString("dateCreated");

                Intent in = new Intent(getApplicationContext(), ProjectTaskMember.class);
                in.putExtra("projectId", projectId);
                in.putExtra("projectTitle", projectTitle);
                in.putExtra("dateCreated", dateCreated);
                startActivity(in);
            }

            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateProject.this);
                builder.setMessage("Are you sure you want to delete the project?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                projectAdapter.deleteItem(position);
                                Toast.makeText(getApplicationContext(), "Deleted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Delete project");
                alert.show();
            }
        });
    }

    public void saveProject(String projectName){
        if(projectName.length() == 0){
            Toast.makeText(CreateProject.this, "Please enter a project title", Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference docRef = db.collection("project").document();
            String projectId = docRef.getId();

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            assert user != null;
            String userId = user.getUid();

            Map<String, Object> project = new HashMap<>();
            project.put("Id", projectId);
            project.put("userId", userId);
            project.put("projectTitle", projectName);
            project.put("dateCreated", date);

            db.collection("project").add(project).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Project Created", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to create", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        projectAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        projectAdapter.stopListening();
    }
}