package com.example.shinji.addressbook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.shinji.addressbook.R;

/**
 * Created by shinji on 2017/08/18.
 */

public class AddressBookContentProvider extends ContentProvider {

    // Create instance of your database
    private AddressBookDatabaseHelper dbHelper;

    // URI Matcher that helps ContentProvider to determine the operation to perform
    // we have two uris one that gives you all rows of contact
    //                  second gives you only one row of contact based on ID

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //Constant values that are used with URIMATCHER to determine the operations to preformed
    private static final int CONTACTS = 2;
    private static final int ONE_CONTACT = 1;


    //static block to configure this URIMAtceher

    static{
        //URI for contact table that will return all rows
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Contact.TABLE_NAME,CONTACTS);

        //uri for contact table with specific #id value
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Contact.TABLE_NAME + "/#"
                ,ONE_CONTACT);

    }

    @Override
    public boolean onCreate() {
        //create the database
        dbHelper = new AddressBookDatabaseHelper(getContext());
        return false;
    }

    //select the data or query database
    // query = similar to SELECT * FROM CONTACT;
    // or SELECT
    @Nullable
    @Override

    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // create a sqllite builder for querying contact table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // you want to wuery to which table that is specified by setTable()
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case ONE_CONTACT :
                //contact with specific ID will be selected here
                //add the ID value from URI to the QueryBuilder
                queryBuilder.appendWhere((DatabaseDescription.Contact._ID + " = " + uri.getLastPathSegment()));
                break;
            case CONTACTS : // all contacts will be selected
                //this is for all rows
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri
                );
        }
        // we just f

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, // Columns & rows
                null, null, sortOrder) ; // GroupBY and Having

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // uri it reperesents table in which data will be added
        // ContentValues it representys the object containg
        //key-value pair
        Uri newContact = null;
        // check whether uri is for contact table
        // if URI is a match for inset
        // then get the writble databse, use the insert()

        switch(uriMatcher.match(uri)){
            //add the new data to the end of table
            // not on specific ID value
            // Contacts give you a ful table
            case CONTACTS :
                //insert
                long rowID = dbHelper.getWritableDatabase().insert(
                        DatabaseDescription.Contact.TABLE_NAME, null, contentValues
                );
                // nullColumnHank : SQLite doesn't support insertin
                // empty row in table, Instead making it illegal to pass
                // empty contentvalues it identifies a columns that a acceot a NULL value.

                //check for success if yes create a uri fo new contact
                if( rowID > 0 ){
                    //create URI
                    newContact = DatabaseDescription.Contact.buildContactUri(rowID);
                    // notify the cursor for newly added data
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    //database operation gets fail throw insert failed exception
                    throw new SQLiteException(
                            getContext().getString(R.string.insert_failed)
                    );
                }
                break;
            default:
                throw new SQLiteException(
                        getContext().getString(R.string.insert_failed)
                );
        }

        return newContact;
    }

    //delete an existing contact
    //Uri means row to delete
    //Where clause specifiy a row to delete
    //String[] having value for where caluse
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int noofRowsDeleted;
        //checj the uri for one contact
        switch (uriMatcher.match(uri)){
            case ONE_CONTACT :
                // get from the uri the id of contact for delete
                String id = uri.getLastPathSegment();
                Log.e("uri::::",String.valueOf(uri));
                Log.e("id::::",String.valueOf(id));
                //delete()
                // 1. Table name
                // 2. Where Clause
                // 3. Array of string subtitute into Where Cluase
                noofRowsDeleted = dbHelper.getWritableDatabase().delete(
                        DatabaseDescription.Contact.TABLE_NAME,
                        DatabaseDescription.Contact._ID + " = " + id,
                        selectionArgs
                );
                break;
            default:
                throw new SQLException(
                        getContext().getString(R.string.invalid_delete_uri)
                );
        }
        //if delete is successfull notify the cursor about data chage
        if(noofRowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return noofRowsDeleted;
    }

    @Override
    // uri : row
    // contetvaluse : key value pair
    // selection : where clause
    // selectionArgs : a string array for wher clause
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int noOfRowsUpdated;
        // we need uri matcher for one contact
        switch ((uriMatcher.match(uri))){
            case ONE_CONTACT :
                // get the id from uri using getlastapatch
                String id = uri.getLastPathSegment();
                //update the data
                // 1. tablename
                // 2. contentvalue
                // 3. where clause
                // 4. values
                noOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        DatabaseDescription.Contact.TABLE_NAME,
                        values,
                        DatabaseDescription.Contact._ID + " = " + id , // e.g. where id = 50
                        selectionArgs
                        );
                break;
            default:
                throw new SQLException(getContext().getString(R.string.invalid_update_uri));
        }
        if(noOfRowsUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return noOfRowsUpdated;
    }
}
