package com.example.shinji.asgviewlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static String EXTRA_INDEX = "SUPER_INDEX";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String listText[] = getResources().getStringArray(R.array.lists_array);
        ArrayAdapter a = new ArrayAdapter<String>(this,R.layout.list_data, listText);
        ListView l = (ListView)findViewById(R.id.view_list);
        l.setAdapter(a);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int itemPosition = position;
                Intent i = new Intent(MainActivity.this, PageActivity.class);
                i.putExtra(EXTRA_INDEX, itemPosition);
                startActivity(i);
                //                i.putExtra(Intent.EXTRA_TEXT, txt.getText().toString());
                //        String itemValue = (String)l.getItemAtPosition(position);
                //        Toast.makeText(getApplicationContext(),
                //                "Position :"+ itemPosition + " ListItem : "+ itemValue,
                //                Toast.LENGTH_LONG).show();
            }

        });
    }
}
