package com.example.googleintegration;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class UpdateTask extends AppCompatActivity {
    private String documentID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String userId;
    private AddNew addNew = new AddNew();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Edit task");

        Intent in = getIntent();

        // Get JSON values from previous intent
        documentID = in.getStringExtra("ID");
        String task = in.getStringExtra("task");
        String date = in.getStringExtra("date");
        String time = in.getStringExtra("time");
        String repeat = in.getStringExtra("repeat");
        String desc = in.getStringExtra("description");

        final EditText txtTask = findViewById(R.id.txtTaskName);
        final EditText txtDate = findViewById(R.id.in_date);
        final EditText txtTime = findViewById(R.id.in_time);
        final Spinner spinner = findViewById(R.id.spinner);
        final EditText txtTaskDesc = findViewById(R.id.txtdesc);
        txtTask.setText(task);
        txtDate.setText(date);
        txtTime.setText(time);
        spinner.setTag(repeat);
        txtTaskDesc.setText(desc);

        Button btnDatePicker = findViewById(R.id.btn_date);
        Button btnTimePicker = findViewById(R.id.btn_time);
        Button btnUpdate = findViewById(R.id.btn_update);

        final Calendar cal = Calendar.getInstance();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTask.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                txtDate.setText(addNew.formatDate(year, monthOfYear + 1, dayOfMonth));
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateTask.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                cal.set(Calendar.MINUTE, minute);
                                cal.set(Calendar.SECOND, 0);

                                txtTime.setText(addNew.FormatTime(hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateTask.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.repeat));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = txtDate.getText().toString();
                String time = txtTime.getText().toString();
                String task = txtTask.getText().toString();
                String repeat = spinner.getSelectedItem().toString();
                String desc = txtTaskDesc.getText().toString();

                if (task.length() == 0 || date.length() == 0 || time.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference docRef = db.collection("reminder").document(documentID);

                    assert user != null;
                    userId = user.getUid();

                    Map<String, Object> reminder = new HashMap<>();
                    reminder.put("userId", userId);
                    reminder.put("task", task);
                    reminder.put("date", date);
                    reminder.put("time", time);
                    reminder.put("repeat", repeat);
                    reminder.put("description", desc);

                    docRef.update(reminder).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });
                    setAlarm(task, repeat, desc, cal.getTimeInMillis());
                    finish();
                }
            }
        });
    }

    public void setAlarm(String task, String repeat, String desc, long dateTime) {
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

        int _id = (int) System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        intent.putExtra("task", task);
        intent.putExtra("description", desc);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), _id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dateTime, repeatInterval, pendingIntent);
    }
}
