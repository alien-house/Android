package com.example.shinji.score;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int scoreA = 0;
    int scoreB = 0;
    TextView scoreATextView;
    TextView scoreBTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreATextView = (TextView) findViewById(R.id.scoreATextView);
        Button btnAPointGet3 = (Button) findViewById(R.id.btnA1);
        Button btnAPointGet2 = (Button) findViewById(R.id.btnA2);
        Button btnAPointGet1 = (Button) findViewById(R.id.btnA3);

        scoreBTextView = (TextView) findViewById(R.id.scoreBTextView);
        Button btnBPointGet3 = (Button) findViewById(R.id.btnB1);
        Button btnBPointGet2 = (Button) findViewById(R.id.btnB2);
        Button btnBPointGet1 = (Button) findViewById(R.id.btnB3);
        Button btnReset = (Button) findViewById(R.id.btn_reset);
        btnReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA = 0;
                scoreB = 0;
                toSetView(scoreATextView,0);
                toSetView(scoreBTextView,0);
            }
        });

        //A
        btnAPointGet3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA += 3;
                toSetView(scoreATextView,scoreA);
            }
        });
        btnAPointGet2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA += 2;
                toSetView(scoreATextView,scoreA);
            }
        });
        btnAPointGet1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreA += 1;
                toSetView(scoreATextView,scoreA);
            }
        });

        //B
        btnBPointGet3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB += 3;
                toSetView(scoreBTextView,scoreB);
            }
        });
        btnBPointGet2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB += 2;
                toSetView(scoreBTextView,scoreB);
            }
        });
        btnBPointGet1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreB += 1;
                toSetView(scoreBTextView,scoreB);
            }
        });

    }

     void toSetView(TextView view, int num){
         view.setText(String.valueOf(num));
     }

}
