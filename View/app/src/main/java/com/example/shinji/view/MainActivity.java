package com.example.shinji.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    String[] mobileArray = {
            "Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Mac OS ","Android","IPhone",
            "WindowsMobile","Blackberry", "WebOS","Ubuntu","Windows7","Mac OS "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter a = new ArrayAdapter<String>(this,R.layout.list_data,mobileArray);
        ListView l = (ListView)findViewById(R.id.mobile_list);
        l.setAdapter(a);
    }
}
