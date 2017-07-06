package com.example.shinji.instant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this, SecondActivity.class);
//                startActivity(i);
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
        TextView txt;
        txt = (TextView) findViewById(R.id.textView2);
    }
}
