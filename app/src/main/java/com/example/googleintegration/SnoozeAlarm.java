package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SnoozeAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_alarm);

        Intent intent = getIntent();
        String documentID = intent.getStringExtra("documentID");
        String task = intent.getStringExtra("task");
        String date = intent.getStringExtra("description");

        Toast.makeText(this, documentID+" "+task+" "+date, Toast.LENGTH_LONG).show();
        TextView txtTask = findViewById(R.id.txtTitle);
        TextView txtTime = findViewById(R.id.txtTime);
        txtTask.setText(task);
        txtTime.setText("");
    }
}