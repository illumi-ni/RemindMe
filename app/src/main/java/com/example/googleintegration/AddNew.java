package com.example.googleintegration;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.googleintegration.NotificationHelper.notificationId;

public class AddNew extends AppCompatActivity {
    private Button btnDatePicker, btnTimePicker, btnSave;
    private EditText txtDate, txtTime, txtTask, txtTaskDesc;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner spinner;
    private DatabaseReference mDatabase;
    private String userId;
    private String documentID;
    private NotificationHelper notificationHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("New Task");

        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        txtTask = findViewById(R.id.txtTaskName);
        spinner= findViewById(R.id.spinner);
        txtTaskDesc = findViewById(R.id.txtdesc);
        btnSave = findViewById(R.id.btn_save);

        notificationHelper = new NotificationHelper(this);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNew.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNew.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNew.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.repeat));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference docRef = db.collection("remainder")
                        .document();
                documentID = docRef.getId();
                assert user != null;
                userId = user.getUid();

                String date = txtDate.getText().toString();
                String time = txtTime.getText().toString();
                String task = txtTask.getText().toString();
                String repeat = spinner.getSelectedItem().toString();
                String desc = txtTaskDesc.getText().toString();

                Map<String, Object> remainder = new HashMap<>();
                remainder.put("Id", documentID);
                remainder.put("userId", userId);
                remainder.put("task", task);
                remainder.put("date", date);
                remainder.put("time", time);
                remainder.put("repeat", repeat);
                remainder.put("description", desc);

                db.collection("remainder").add(remainder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Added task to list",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to add task",Toast.LENGTH_SHORT).show();
                }});

                sendNotification(txtTask.getText().toString(), txtTaskDesc.getText().toString());


            }
        });
    }

    public void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void sendNotification(String Title, String description){
        NotificationCompat.Builder nb = notificationHelper.getNotificationChannel(Title, description);
        notificationHelper.getManager().notify(1, nb.build());
    }
}