package com.example.shinji.musicplayer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by shinji on 2017/08/12.
 */

public class MusicService extends Service implements View.OnClickListener, MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener  {

    public static MediaPlayer myPlayer;

    public static WeakReference textViewSongTime;
    public static WeakReference songProgressBar;
    static Handler progressBarHandler = new Handler();
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
        initUI();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initUI() {
//        btnPlay = new WeakReference (MainActivity.btnPlay);
//        btnStop = new WeakReference (MainActivity.btnStop);
//        textViewSongTime = new WeakReference&lt;&gt;(MainActivity.textViewSongTime);
        songProgressBar = new WeakReference (R.id.seekBar);
        songProgressBar.get().setOnSeekBarChangeListener(this);
//        btnPlay.get().setOnClickListener(this);
//        btnStop.get().setOnClickListener(this);
//        mp.setOnCompletionListener(this);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        myPlayer.stop();
        myPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        songProgressBar.get().setProgress(0);
        progressBarHandler.removeCallbacks(mUpdateTimeTask); /* Progress Update stop */

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    public void updateProgressBar(){
        try{
            progressBarHandler.postDelayed(mUpdateTimeTask, 100);
        }catch(Exception e){

        }
    }

    static Runnable mUpdateTimeTask = new Runnable() {
        public void run(){
            long totalDuration = 0;
            long currentDuration = 0;

            try {
                totalDuration = myPlayer.getDuration();
                currentDuration = myPlayer.getCurrentPosition();
//                textViewSongTime.get().setText(Utility.milliSecondsToTimer(currentDuration) + "/" + Utility.milliSecondsToTimer(totalDuration)); // Displaying time completed playing
                int progress = (int)(Utility.getProgressPercentage(currentDuration, totalDuration));
                songProgressBar.get().setProgress(progress);	/* Running this thread after 100 milliseconds */
                progressBarHandler.postDelayed(this, 100);

            } catch(Exception e){
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progressBarHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressBarHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = myPlayer.getDuration();
        int currentPosition = Utility.progressToTimer(seekBar.getProgress(),totalDuration);
        myPlayer.seekTo(currentPosition);
        updateProgressBar();

    }
}
