package com.example.googleintegration;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateTask extends AppCompatActivity {
    private String documentID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnDatePicker, btnTimePicker, btnUpdate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Task");

        Intent in = getIntent();

        // Get JSON values from previous intent
        documentID = in.getStringExtra("ID");
        String task = in.getStringExtra("task");
        String date = in.getStringExtra("date");
        String time = in.getStringExtra("time");
        String repeat = in.getStringExtra("repeat");
        String desc = in.getStringExtra("description");

        final EditText txtTask = (EditText) findViewById(R.id.txtTaskName);
        final EditText txtDate = (EditText) findViewById(R.id.in_date);
        final EditText txtTime = (EditText) findViewById(R.id.in_time);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final EditText txtTaskDesc = (EditText) findViewById(R.id.txtdesc);
        txtTask.setText(task);
        txtDate.setText(date);
        txtTime.setText(time);
        spinner.setTag(repeat);
        txtTaskDesc.setText(desc);

        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        btnUpdate = findViewById(R.id.btn_update);

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

                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateTask.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateTask.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.repeat));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference docRef = db.collection("reminder")
                        .document(documentID);
                assert user != null;
                userId = user.getUid();

                String date = txtDate.getText().toString();
                String time = txtTime.getText().toString();
                String task = txtTask.getText().toString();
                String repeat = spinner.getSelectedItem().toString();
                String desc = txtTaskDesc.getText().toString();

                Map<String, Object> reminder = new HashMap<>();
                reminder.put("userId", userId);
                reminder.put("task", task);
                reminder.put("date", date);
                reminder.put("time", time);
                reminder.put("repeat", repeat);
                reminder.put("description", desc);

                docRef.update(reminder)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
