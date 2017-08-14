package com.example.shinji.musicplayer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shinji on 2017/08/12.
 */

public class MusicService extends Service {

    MediaPlayer myPlayer;

//    public MusicService(){
//        super("MusicService");
//        Log.e("MusicService","constracuter");
//    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        Log.e("MusicService","onHandleIntent");
//
//    }

    @Override
    public void onCreate() {
        Log.e("MusicService","MusicService");
        super.onCreate();
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        myPlayer = MediaPlayer.create(this, R.raw.newromantics);
        myPlayer.setLooping(true); // Set looping

        myPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        myPlayer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
//        System.out.println("myPlayer:" + myPlayer);
//        myPlayer.start();
//        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        myPlayer.stop();
        myPlayer.release();
    }

}
