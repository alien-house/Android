package com.example.shinji.sqldatabases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinji on 2017/08/15.
 *
 */

public class BookAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    List<Integer> listID = new ArrayList<Integer>();
    List<String> listTitle = new ArrayList<String>();
    List<String> listAuthor = new ArrayList<String>();

    BookAdapter(MainActivity mainActivity, List listID, List listTitle, List listAuthor){
        this.context = mainActivity;
        this.listID = listID;
        this.listTitle = listTitle;
        this.listAuthor = listAuthor;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listID.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        // Loading a view or attaching a view
        View rowView = layoutInflater.inflate(R.layout.listview_row, null);
        // binding of views to variables
        holder.bookID = (TextView) rowView.findViewById(R.id.itemId);
        holder.booktitle = (TextView) rowView.findViewById(R.id.itemTitle);
        holder.bookauthor = (TextView)rowView.findViewById(R.id.itemAuthor);
        holder.bookID.setText(listID.get(position).toString());
        holder.booktitle.setText(listTitle.get(position));
        holder.bookauthor.setText(listAuthor.get(position));
        return  rowView;
    }

    public class Holder{
        TextView booktitle, bookauthor, bookID;
    }
}
