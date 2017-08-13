package com.example.shinji.asynctaskex;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String[] cities = {"usa","africa","usa","africa"};
    ListView list_Cities;
    ProgressBar progress_cities;
    TextView text_Cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_Cities = (ListView) findViewById(R.id.listviewcities);
        progress_cities = (ProgressBar) findViewById(R.id.progresslistitems);
        text_Cities = (TextView) findViewById(R.id.textviewprogress);
        ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        list_Cities.setAdapter(a);
        new MyTask().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    class MyTask extends AsyncTask<Void, String, Void>{

        ArrayAdapter<String> adpter;
        private int count = 0;

        @Override
        protected void onPreExecute() {
//            super.onPreExecute();
            adpter = (ArrayAdapter<String>) list_Cities.getAdapter();
            progress_cities.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(String item: cities) {
                publishProgress(item);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adpter.add(values[0]);
            count++;

            int val_progress = (int)((( (double)count / (double)cities.length )) * 100);
            Log.e("test:", String.valueOf((double)(1/4D)));
            Log.e("test:", String.valueOf((double)(1/4)));
            Log.e("count:", String.valueOf(count));
            Log.e("cities.length:", String.valueOf(cities.length));
            Log.e("val_progress:", String.valueOf(val_progress));
            progress_cities.setProgress(val_progress);
            text_Cities.setText("UPDATING..." + count + " with " + val_progress + "%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "ALL TIME AND SUCESSFFLY", Toast.LENGTH_SHORT).show();
            progress_cities.setVisibility(View.GONE);
        }
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
