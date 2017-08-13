package com.example.shinji.asglayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by shinji on 2017/07/06.
 */

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_layout);

        String[] StringArray = {
                "Android",
                "iPhone",
                "WindowsMobile",
                "Blackberry",
                "WebOS",
                "Ubuntu",
                "Windows7",
                "Mac OS X"
        };
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, StringArray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

    }

}
