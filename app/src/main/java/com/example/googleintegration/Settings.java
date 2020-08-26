package com.example.googleintegration;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load setting fragment
//        getFragmentManager().beginTransaction().replace(android.R.id.content,
//                new MainSettingFragment()).commit();
        addPreferencesFromResource(R.xml.preferences);
        Load_settings();

    }

//    public static class MainSettingFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//           addPreferencesFromResource(R.xml.preferences);
//
//        }
//    }

    public void Load_settings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chk_night = sp.getBoolean("NIGHT", false);
        if (chk_night) {
            getListView().setBackgroundColor(Color.parseColor("#555555"));
        } else {
            getListView().setBackgroundColor(Color.parseColor("#ffffff"));
        }


        CheckBoxPreference chk_night_instant = (CheckBoxPreference) findPreference("NIGHT");
        chk_night_instant.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference prefs, Object o) {
                boolean yes = (boolean) o;
                if (yes) {
                    getListView().setBackgroundColor(Color.parseColor("#555555"));
                } else {
                    getListView().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return true;
            }


        });


//        ListPreference LP = (ListPreference) findPreference("ORIENTATAION");
//        String orien = sp.getString("ORIENTATAION", "false");
//        if ("1".equals(orien)) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
//            LP.setSummary(LP.getEntry());
//        } else if ("2".equals(orien)) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            LP.setSummary(LP.getEntry());
//        } else if ("3".equals(orien)) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            LP.setSummary(LP.getEntry());
//        }
//
//        LP.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(android.preference.Preference prefs, Object o) {
//                String items = (String) o;
//                if (prefs.getKey().equals("ORIENTATAION")) {
//                    switch (items) {
//                        case "1":
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
//                            break;
//                        case "2":
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                            break;
//                        case "3":
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                            break;
//                    }
//                    ListPreference LPP = (ListPreference) prefs;
//                    LPP.setSummary(LPP.getEntries()[LPP.findIndexOfValue(items)]);
//                }
//                return true;
//            }
//        });

    }
    @Override
    protected void onResume() {
        Load_settings();
        super.onResume();
    }

}

