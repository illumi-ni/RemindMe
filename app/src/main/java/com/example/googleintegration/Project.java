package com.example.googleintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googleintegration.models.ProjectNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Project extends AppCompatActivity implements ProjectActivity {
    private TextView prtname;
    private TextView prtdesc;
    private Button btnprt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setTitle("Project");

        prtname = (TextView) findViewById(R.id.txtprjtname);
        prtdesc = (TextView) findViewById(R.id.txtprjtdesc);

        //opens project form
        Button btnpr = findViewById(R.id.btnproject);
        btnpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openDialog();
            }
        });
    }
    public void openDialog(){
        CreateProject createProject = new CreateProject();
        createProject.show(getSupportFragmentManager(), "create project");
    }

    @Override
    public void createproject(String projectname, String projectdesc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newProjectRef = db.collection("projects").document();

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProjectNote projectNote = new ProjectNote();
        projectNote.setProjectname(projectname);
        projectNote.setProjectdesc(projectdesc);
        projectNote.setProject_id(newProjectRef.getId());
        projectNote.setUser_id(user_id);

        newProjectRef.set(projectNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Project.this, "Created new project", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Project.this, "Failed to create project", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}