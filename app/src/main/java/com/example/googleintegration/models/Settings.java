package com.example.googleintegration.models;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar;
import android.content.Context;
import com.example.googleintegration.R;


public class Settings extends AppCompatActivity {
    RadioButton slient, vibrate, ring;
    SeekBar lightBar;
    Context context;
    int brightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ring = (RadioButton)findViewById(R.id.rdbRing);
        slient = (RadioButton)findViewById(R.id.rdbSlient);
        vibrate = (RadioButton)findViewById(R.id.rdbVibrate);
//        final ToggleButton tb1 = (ToggleButton)findViewById(R.id.toggle1);
        final ToggleButton tb2 = (ToggleButton)findViewById(R.id.toggle2);
        tb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Notification - " + tb2.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        lightBar = findViewById(R.id.seekBar);
        context = getApplicationContext();
        brightness =
                android.provider.Settings.System.getInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);
        lightBar.setProgress(brightness);


    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String str="";
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rdbRing:
                if(checked)
                    str = "Ring Mode";
                break;

            case R.id.rdbSlient:
                if(checked)
                    str = "Slient Mode";
                break;
            case R.id.rdbVibrate:
                if(checked)
                    str = "Vibrate Mode";
                break;
        }
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();


    }
}
