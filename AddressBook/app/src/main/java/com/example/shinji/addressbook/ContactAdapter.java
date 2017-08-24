package com.example.shinji.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shinji.addressbook.data.DatabaseDescription;

/**
 * Created by shinji on 2017/08/21.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    //1. create a inner class a view for each row of recyclerview
    // nested subclass of RecyclerView.ViewHolder used to implement
    // the view-holder pattern in the context of a RecyclerView
    private Cursor cursor = null;
    public interface ContactAdapterInterface{
        void onClick(Uri uri);
    }

    //constructor
    public ContactAdapter(ContactAdapterInterface clickListener) {
        this.contactAdapterInterface = clickListener;
    }
    public ContactAdapterInterface contactAdapterInterface;


    public void notifyChange(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create  a simple list item
        // inflate the android.R.layout.simple_list_item_1 layout
        Context context = parent.getContext();
        //create a lauout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        ContactViewHolder viewholder = new ContactViewHolder(view);

        return new ContactViewHolder(view); // return current item's ViewHolder
    }

    @Override
    //create a cursor object
    //sets the text of the list item to display the search
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        cursor.moveToPosition(position);
        holder.textContact.setText(cursor.getString(cursor.getColumnIndex(DatabaseDescription.Contact.COLUMN_NAME)));
        holder.setRowID(cursor.getLong(cursor.getColumnIndex(DatabaseDescription.Contact._ID)));
    }

    @Override
    public int getItemCount() {
        //use getcount methods of cursor object & it retuns no. of rows inc cursor
//        return rows;
        int count = 0;
        if(cursor != null){
            count = cursor.getCount();
        }
        return count;
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder{
        //Recyclerview is showing the name only, ID is just needed for database.
        public TextView textContact;
        private long rowID;

        // configures a RecyclerView item's ViewHolder
        public ContactViewHolder(View itemView) {
            super(itemView);
            //dynamically create textview
            textContact = (TextView) itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(
                    new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {

                            //adapter is attached with contactFragment
                            //so implemnet the interface in ContactFragment
                            //not in MainActivity
                            contactAdapterInterface.onClick(
                                    DatabaseDescription.Contact
                                            .buildContactUri(rowID));
                            //buildContactUri() that create a uri
//with selected rowID
                        }
                    }
            );
        }

        // set the database row ID for the contact in this ViewHolder
        public void setRowID(long rowID){
            this.rowID = rowID;
        }

    }
}
