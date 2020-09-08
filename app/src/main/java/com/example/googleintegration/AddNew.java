package com.example.googleintegration;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNew extends AppCompatActivity {
    private EditText txtDate, txtTime, txtTask, txtTaskDesc;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Spinner spinner;
    private String userId;
    private String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("New Task");

        Button btnDatePicker = findViewById(R.id.btn_date);
        Button btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        txtTask = findViewById(R.id.txtTaskName);
        spinner = findViewById(R.id.spinner);
        txtTaskDesc = findViewById(R.id.txtdesc);
        Button btnSave = findViewById(R.id.btn_save);

        final Calendar cal = Calendar.getInstance();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNew.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                String date = formatDate(year, monthOfYear + 1, dayOfMonth);
                                txtDate.setText(date);
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
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                cal.set(Calendar.SECOND, 0);

                                String time = FormatTime(hourOfDay, minute);
                                txtTime.setText(time);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddNew.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.repeat));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = txtTask.getText().toString();
                String date = txtDate.getText().toString();
                String time = txtTime.getText().toString();
                String repeat = spinner.getSelectedItem().toString();
                String desc = txtTaskDesc.getText().toString();

                if (task.length() == 0 || date.length() == 0 || time.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference docRef = db.collection("reminder").document();
                    documentID = docRef.getId();

                    assert user != null;
                    userId = user.getUid();

                    int alarmID = (int) System.currentTimeMillis();

                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("Id", documentID);
                    reminder.put("userId", userId);
                    reminder.put("task", task);
                    reminder.put("date", date);
                    reminder.put("time", time);
                    reminder.put("repeat", repeat);
                    reminder.put("description", desc);
                    reminder.put("alarmID", alarmID);

                    db.collection("reminder").add(reminder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Added task to list", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to add task", Toast.LENGTH_SHORT).show();
                        }
                    });
                    setAlarm(documentID, task, repeat, desc, cal.getTimeInMillis(), alarmID);
                    finish();
                }
            }
        });
    }

    public void setAlarm(String documentID, String task, String repeat, String desc, long dateTime, int alarmID) {
        int repeatInterval = 0;
        switch(repeat){
            case "Every minute":
                repeatInterval = 60000;
                break;

            case "Every hour":
                repeatInterval = 3600000;
                break;

            case "Every day":
                repeatInterval = 86400000;
                break;

            case "Every week":
                repeatInterval = 604800000;
                break;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        intent.putExtra("documentID", documentID);
        intent.putExtra("task", task);
        intent.putExtra("description", desc);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), alarmID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dateTime, repeatInterval, pendingIntent);
    }

    public String FormatTime(int hour, int minute) {
        String time;
        String newMinute;

        if (minute < 10) {
            newMinute = "0" + minute;
        } else {
            newMinute = String.valueOf(minute);
        }

        if (hour == 0) {
            time = "12" + ":" + newMinute + " " + "AM";
        } else if (hour < 10) {
            time = "0" + hour + ":" + newMinute + " " + "AM";
        } else if (hour < 12) {
            time = hour + ":" + newMinute + " " + "AM";
        } else if (hour == 12) {
            time = "12" + ":" + newMinute + " " + "PM";
        } else {
            int temp = hour - 12;
            String tm;
            if (temp < 10) {
                tm = "0" + temp;
            } else {
                tm = String.valueOf(temp);
            }
            time = tm + ":" + newMinute + " " + "PM";
        }
        return time;
    }

    public String formatDate(int year, int monthOfYear, int dayOfMonth) {
        String date;
        String newMonth;
        if (monthOfYear < 10) {
            newMonth = "0" + monthOfYear;
        } else {
            newMonth = String.valueOf(monthOfYear);
        }

        String newDay;
        if (dayOfMonth < 10) {
            newDay = "0" + dayOfMonth;
        } else {
            newDay = String.valueOf(dayOfMonth);
        }
        date = year + "-" + newMonth + "-" + newDay;
        return date;
    }
}
