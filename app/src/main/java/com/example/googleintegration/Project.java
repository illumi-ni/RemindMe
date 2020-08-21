package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class Project extends AppCompatActivity {
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
}