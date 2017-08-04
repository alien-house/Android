package com.example.shinji.preference2;

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
