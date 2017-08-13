package com.example.shinji.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v7.appcompat.R.styleable.View;

/**
 * Created by shinji on 2017/07/13.
 */

public class PopUpMenuEx extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupmenu);
    }

    public void showPopUpMenu(View view){
        PopupMenu m = new PopupMenu(this, view);
        //Inflating the Popup using xml file
        MenuInflater i = m.getMenuInflater();
        i.inflate(R.menu.context_menu,m.getMenu());
        // This activity implements OnMenuItemClickListener
        m.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_bookmark:
                        // Single menu item is selected do something
                        // Ex: launching new activity/screen or show alert message
                        Toast.makeText(PopUpMenuEx.this, "Bookmark" +
                                " is Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_save:
                        Toast.makeText(PopUpMenuEx.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_search:
                        Toast.makeText(PopUpMenuEx.this, "Search is Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_share:
                        Toast.makeText(PopUpMenuEx.this, "Share is Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_delete:
                        Toast.makeText(PopUpMenuEx.this, "Delete is Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_preferences:
                        Toast.makeText(PopUpMenuEx.this, "Preferences is Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        m.show();//showing popup menu
    }
}
