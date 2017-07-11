package com.example.shinji.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by shinji on 2017/07/11.
 */

public class SimpleMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm);

        String listText[] = getResources().getStringArray(R.array.menu_items);
        System.out.println("================");
        System.out.println(listText);
        ArrayAdapter a = new ArrayAdapter<String>(this,R.layout.list_data,listText);
        ListView l = (ListView)findViewById(R.id.ListView_menu);
        l.setAdapter(a);
    }
}
