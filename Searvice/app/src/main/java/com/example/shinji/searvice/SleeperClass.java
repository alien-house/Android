package com.example.shinji.searvice;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shinji on 2017/08/09.
 */

public class SleeperClass extends IntentService {
    final static String TAG = "SleeperClass";
    long seconds;
    public SleeperClass(){
        super("SleeperClass");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        seconds = intent.getExtras().getLong("seconds");
        long milliSeconds = seconds * 1000;
        try{
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Toast.makeText(this,"Thread is Destroyed",Toast.LENGTH_LONG).show();
    }
}
