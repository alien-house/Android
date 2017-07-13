package com.example.shinji.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shinji on 2017/07/13.
 */

public class ListContextMenu extends AppCompatActivity {

    String[] mobileArray = {
            "Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Mac OS ","Android","IPhone",
            "WindowsMobile","Blackberry", "WebOS","Ubuntu","Windows7","Mac OS "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);//大元のページのレイアウト表示：今回リストビューだけある
        ArrayAdapter a = new ArrayAdapter<String>(this,R.layout.list_data,mobileArray);//各リストの中身のテンプレ：今回テキストのみ
        ListView l = (ListView)findViewById(R.id.mobile_list);//ListViewだけを抽出
        l.setAdapter(a);

        registerForContextMenu(l);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.context_menu,menu);//コンテキストメニューの内容、一覧のリソース
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_bookmark)
            Toast.makeText(this, "bookmark is selected",Toast.LENGTH_SHORT).show();
        if(id == R.id.menu_save)
            Toast.makeText(this, "save is selected",Toast.LENGTH_SHORT).show();
        if(id == R.id.menu_search)
            Toast.makeText(this, "search is selected",Toast.LENGTH_SHORT).show();
        if(id == R.id.menu_share)
            Toast.makeText(this, "share is selected",Toast.LENGTH_SHORT).show();
        if(id == R.id.menu_delete)
            Toast.makeText(this, "delete is selected",Toast.LENGTH_SHORT).show();
        if(id == R.id.menu_preferences)
            Toast.makeText(this, "preferences is selected",Toast.LENGTH_SHORT).show();
        return true;
    }

}
