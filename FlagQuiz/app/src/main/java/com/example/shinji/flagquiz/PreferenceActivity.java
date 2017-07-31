package com.example.shinji.flagquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shinji on 2017/07/28.
 */

public class PreferenceActivity extends AppCompatActivity {
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferenceFragment()).commit();
    }
}
