package com.example.shinji.preference2;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Created by shinji on 2017/07/28.
 */

public class EditPreferences extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}