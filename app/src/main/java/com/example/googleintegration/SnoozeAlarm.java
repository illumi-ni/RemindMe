package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SnoozeAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_alarm);

        Intent intent = getIntent();
        final String documentID = intent.getStringExtra("documentID");
        final String task = intent.getStringExtra("task");
        final String desc = intent.getStringExtra("description");
        final int alarmID = intent.getIntExtra("alarmID", 0);

        TextView txtTask = findViewById(R.id.txtTitle);
        final TextView txtTime = findViewById(R.id.txtTime);
        txtTask.setText(task);

        Date curDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        final String currTime = (formatter.format(curDate));

        txtTime.setText(currTime);

        Button btnSnooze = findViewById(R.id.btnSnooze);
        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSnooze(documentID, task, desc, alarmID);
                Snackbar.make(findViewById(R.id.cons),
                        "Alarm snoozed for 2 minutes!",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(alarmID);
                finish();
            }
        });
    }

    public void setSnooze(String documentID, String task, String desc, int alarmID) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        intent.putExtra("documentID", documentID);
        intent.putExtra("task", task);
        intent.putExtra("description", desc);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), alarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, 120000, pendingIntent);
    }

    public void cancelAlarm(int alarmID){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), alarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}