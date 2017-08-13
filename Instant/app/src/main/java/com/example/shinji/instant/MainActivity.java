package com.example.shinji.instant;

import android.content.Intent;
import android.net.Uri;
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

//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.ca/"));

                String subject = "kokoko";
                String boby = "this is the body text";

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto","ucyuujinoco@gmail.com",null));
                i.putExtra(Intent.EXTRA_SUBJECT, subject);
                i.putExtra(Intent.EXTRA_TEXT, boby);
                startActivity(Intent.createChooser(i, "Send email"));

//                Intent i = new Intent(MainActivity.this, SecondActivity.class);
//                i.putExtra(Intent.EXTRA_TEXT, txt.getText().toString());
//
//                System.out.println("========================");
//                System.out.println(txt.getText());

//                startActivity(i);
//                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
