package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ProjectTaskMember extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task_member);

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
        String projectTitle = intent.getStringExtra("projectTitle");
        String dateCreated = intent.getStringExtra("dateCreated");
        setTitle(projectTitle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}