package com.example.shinji.preference;

import android.os.Bundle;
/**
 * Created by shinji on 2017/07/28.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
