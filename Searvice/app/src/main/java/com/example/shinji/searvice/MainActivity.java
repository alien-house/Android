package com.example.shinji.searvice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Button buttonStart, buttonStop,buttonNext;
//    MediaPlayer myPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonStart:
                Intent i= new Intent(this, MyService.class);
                startService(i);
                break;
            case R.id.buttonStop:
                i = new Intent(this,MyService.class);
                stopService(i);
                break;
            case R.id.buttonNext:
                i = new Intent(this,NextClass.class);
                startActivity(i);
                break;
        }

    }
}
