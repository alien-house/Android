package com.example.shinji.lifcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String msg = "GA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(msg, "onCreate");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textview = (TextView) findViewById(R.id.textView2);
                textview.setText("Testeststest");
            }
        });

    }


    //Called when activity is about to become visible
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(msg, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(msg, "onResume");
    }

    //Called another activity is taking focus
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(msg, "onPause");
    }

    //
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(msg, "onStop");
    }

    // when just before the activity is destroyed
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(msg, "onDestroy");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(msg, "onRestart");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
