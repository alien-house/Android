package com.example.shinji.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by shinji on 2017/07/11.
 */

public class SimpleMenuActivity extends AppCompatActivity {
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm);

        String listText[] = getResources().getStringArray(R.array.menu_items);
        System.out.println("================");
        System.out.println(listText);
        ArrayAdapter a = new ArrayAdapter<String>(this,R.layout.list_data,listText);
        l = (ListView)findViewById(R.id.ListView_menu);
        l.setAdapter(a);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String)l.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Position :"+ itemPosition + " ListItem : "+ itemValue,
                        Toast.LENGTH_LONG).show();
            }

        });

    }
}
