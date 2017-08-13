package com.example.shinji.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by shinji on 2017/07/13.
 */

public class CheckableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupmenu);
    }
    // you can inflate your menu resource
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create menu : instantiate menu XML files into Menu objects
        getMenuInflater().inflate(R.menu.check_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.red) {
            // change state of check
            item.setChecked(!item.isChecked());
            // get a state
            boolean checked = item.isChecked();
           return false;
        }else if(id == R.id.blue){
            // change state of check
            item.setChecked(!item.isChecked());
            // get a state
            boolean checked = item.isChecked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
