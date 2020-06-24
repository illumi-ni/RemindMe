package com.example.googleintegration;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class AddNew extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        Toast.makeText(AddNew.this, "All Sign In Failed!", Toast.LENGTH_SHORT).show();
    }
}