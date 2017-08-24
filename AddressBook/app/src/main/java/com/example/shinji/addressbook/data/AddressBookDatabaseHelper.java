package com.example.shinji.addressbook.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

/**
 * Created by shinji on 2017/08/18.
 */

// this class creates and upgreate the database

public class AddressBookDatabaseHelper extends SQLiteOpenHelper {

    //create a database name and version
    public static final String DatabaseName = "AddressBook";
    public static final int DatabaseVersion = 1;

    //create a default constructor
    public AddressBookDatabaseHelper(Context context){
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL query for creating the contact table
        final String CREATE_CONTACTS_TABLE = "CREATE TABLE " +
                DatabaseDescription.Contact.TABLE_NAME + " (" +
                DatabaseDescription.Contact._ID + " integer primary key," +
                DatabaseDescription.Contact.COLUMN_NAME + " TEXT" + ", " +
                DatabaseDescription.Contact.COLUMN_PHONE + " TEXT" + ", " +
                DatabaseDescription.Contact.COLUMN_EMAIL + " TEXT" + ", " +
                DatabaseDescription.Contact.COLUMN_STREET + " TEXT" + ", " +
                DatabaseDescription.Contact.COLUMN_CITY + " TEXT" + ", " +
                DatabaseDescription.Contact.COLUMN_STATE + " TEXT" + ", " +
                DatabaseDescription.Contact.COLUMN_ZIP + " TEXT" + ");";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseDescription.Contact.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
