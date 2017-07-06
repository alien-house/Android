package com.example.shinji.instant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView txtview;
        txtview = (TextView) findViewById(R.id.textView2);


        txt = (EditText) findViewById(R.id.txt);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, txt.getText().toString());
//                i.putExtra(Intent.EXTRA_TEXT,"This is the");

                System.out.println("========================");
                System.out.println(txt.getText());


//                i.setAction(Intent.ACTION_SEND);
//                i.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                i.setType("text/plain");
//                startActivity(Intent.createChooser(i, getResources().getText(R.string.send_to)));
//
//



                startActivity(i);
//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
