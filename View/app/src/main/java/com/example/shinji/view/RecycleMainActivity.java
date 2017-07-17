package com.example.shinji.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by shinji on 2017/07/17.
 */

public class RecycleMainActivity extends AppCompatActivity {

    private static final int LIST_COUNT = 100;
    private GreenAdapter mAdpter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerlayout_main); //Recycle view
        rv = (RecyclerView) findViewById(R.id.rv1);
        LinearLayoutManager lv = new LinearLayoutManager(this);
        rv.setLayoutManager(lv);
        mAdpter = new GreenAdapter(LIST_COUNT);
        rv.setAdapter(mAdpter);
    }

}




