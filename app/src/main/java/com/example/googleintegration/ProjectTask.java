package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProjectTask extends AppCompatActivity {
    private TextView prtskname;
    private TextView prtskdesc;
    private FloatingActionButton fabtsk, fabinvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task);

        fabtsk = (FloatingActionButton) findViewById(R.id.fab_project_task);
        fabtsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    public void openDialog(){
        CreateProjectTask createProjectTask = new CreateProjectTask();
        createProjectTask.show(getSupportFragmentManager(), "project task");
    }

}