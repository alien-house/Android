package com.example.shinji.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by shinji on 2017/07/12.
 */

public class GridViewActivity extends AppCompatActivity {
    GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridviewexample);
        gridview = (GridView) findViewById(R.id.gv1);
        gridview.setAdapter(new ImageAdapter(this));
        //use setOnItemClickListener on gridview and make a toast for item click event
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(GridViewActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
