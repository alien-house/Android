package com.example.shinji.jobsearchapp;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnSearch;
    final String URL_INDEED = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView txtSearchLocation = (TextView) findViewById(R.id.searchLocation);
                TextView txtSearchWord = (TextView) findViewById(R.id.searchWord);

//                if(txtSearchLocation.getText().toString().matches("")){
//                    Toast.makeText(getApplicationContext(), "You did not enter a location", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(txtSearchWord.getText().toString().matches("")){
//                    Toast.makeText(getApplicationContext(), "You did not enter any words", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Intent preferencesIntent = new Intent(MainActivity.this, ResultActivity.class);
                System.out.println( "=========================" );
//                System.out.println( txtSearchLocation.getText());
//                System.out.println( txtSearchWord.getText());
//                preferencesIntent.putExtra("SEARCH_LOC", txtSearchLocation.getText().toString());
//                preferencesIntent.putExtra("SEARCH_WORD", txtSearchWord.getText().toString());
                startActivity(preferencesIntent);

            }
        });




    }



}
