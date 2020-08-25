package com.example.googleintegration;
import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
public class Settings extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load setting fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainSettingFragment()).commit();
    }
    public static class MainSettingFragment extends PreferenceFragment{
        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
