package com.example.shinji.flagquiz;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

/**
 * Created by shinji on 2017/07/28.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

//        MultiSelectListPreference prefListThemes = (MultiSelectListPreference) findPreference("pref_regionsToInclude");
//
//        prefListThemes.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//
//                Log.e("quiz ", "=================================");
//                return true;
//            }
//        });

    }


//
//    PreferenceFragment myPref = (PreferenceFragment) findPreference("numberType");
//        myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//        public boolean onPreferenceClick(Preference preference) {
//            //open browser or intent here
//        }
//    });
}
