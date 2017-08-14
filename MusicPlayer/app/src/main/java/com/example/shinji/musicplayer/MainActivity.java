package com.example.shinji.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer player;
    ImageButton playButton, stopButton, resetButton;
    boolean play_reset = true;
    private SeekBar seekbar;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = MediaPlayer.create(this, R.raw.newromantics);

        playButton = (ImageButton) this.findViewById(R.id.play);
        stopButton = (ImageButton) this.findViewById(R.id.stop);
        resetButton = (ImageButton) this.findViewById(R.id.reset);
        seekbar = (SeekBar)findViewById(R.id.seekBar);

        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Log.e("onClick","View");
        switch (v.getId()) {
            case R.id.play:
                if (play_reset) {
                    play_reset = false;
                    Intent intent = new Intent(this, MusicService.class);
                    startService(intent);
//                    player.setLooping(false);
                }
                playPause();
                Log.e("onClick","kiteru");
                break;
            case R.id.stop:
                if (!play_reset) {
//                    player.stop();
                    intent = new Intent(this, MusicService.class);
                    stopService(intent);
                    playButton.setImageResource(R.drawable.buttonplay);
                    Toast.makeText(this, R.string.stopped, Toast.LENGTH_SHORT).show();
                    try {
                        player.prepare();
                    } catch (IllegalStateException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.reset:
                if (!play_reset) {
                    player.reset();
                    playButton.setImageResource(R.drawable.buttonplay);
                    Toast.makeText(this, R.string.reset, Toast.LENGTH_SHORT).show();
                    player.release();
                    play_reset = true;
                    break;
                }
        }
    }

    // when the activity is paused, then pause the music playback
    @Override
    public void onPause() {
        super.onPause();
        player.reset();
// change the image of the play button to play
        playButton.setImageResource(R.drawable.play);
        Toast.makeText(this, R.string.reset, Toast.LENGTH_SHORT).show();
// Release media instance to system
        player.release();
        play_reset = true;
    }

    // Toggle between the buttonplay and pause
    private void playPause() {
// if the music is playing then pause the music playback
        if(player.isPlaying()) {
            player.pause();
// change the image of the play button to play
            playButton.setImageResource(R.drawable.buttonplay);
            Toast.makeText(this, R.string.paused, Toast.LENGTH_SHORT).show();
        }
// Music is paused, start, or resume playback
        else {
// change the image of the play button to pause
            playButton.setImageResource(R.drawable.pause);
            player.start();
            Toast.makeText(this, R.string.isPlaying, Toast.LENGTH_SHORT).show();
        }
    }

}
