package com.example.shinji.jobsearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by shinji on 2017/07/31.
 */

public class ResultActivity extends AppCompatActivity {
    private ArrayList<Job> joblist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_main);
        makeFakeDate();

    }

    void makeFakeDate(){
        joblist.add( new Job("Web Developler", "http://google.com/", "koooo","korekorekore", "2days ago"));
        joblist.add( new Job("Front Developler", "http://rrrr.com/", "koooo","kiieure", "3days ago"));
        joblist.add( new Job("Back Developler", "http://dede.com/", "tste","eeee", "5days ago"));
    }



}

