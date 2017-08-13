package com.example.shinji.searvice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    Button buttonStart, buttonStop,buttonNext,buttonIntent,buttonHandler;
    EditText textEdit;
    Long secondsLong;
    private Handler handler;
    private ProgressBar progressBar;
//    MediaPlayer myPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonIntent = (Button) findViewById(R.id.buttonIntent);
        textEdit = (EditText) findViewById(R.id.textEdit);
        buttonHandler = (Button) findViewById(R.id.buttonHandler);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        handler = new Handler();
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonIntent.setOnClickListener(this);
        buttonHandler.setOnClickListener(this);
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
                startService(i);
                break;
            case R.id.buttonIntent:
                secondsLong = Long.parseLong(textEdit.getText().toString());
                i = new Intent(this, SleeperClass.class);
                i.putExtra("seconds", secondsLong);
                startService(i);
                break;
            case R.id.buttonHandler:
                Toast.makeText(getBaseContext(), "Handler Start",
                        Toast.LENGTH_LONG).show();
                startProgress();
                break;
        }

    }

    public void startProgress(){
            new Thread(new Task()).start();
    }

     class Task implements Runnable{
         @Override
         public void run() {
             for (int i = 0; i <= 20; i++) {
                 final int value = i;
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         progressBar.setProgress(value);
                     }
                 });
             }
         }
     }
}
