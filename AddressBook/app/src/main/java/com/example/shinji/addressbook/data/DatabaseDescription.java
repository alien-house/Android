package com.example.shinji.addressbook.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shinji on 2017/08/16.
 */

// This class is providing content provider's name : Authority
    // URI to interact with Contentprovider
    // Inner class Contact that describes tablename, column names
public class DatabaseDescription {
    //ContentProcider's name : typically package name
    //Authority should be package name
    public static final String AUTHORITY = "com.example.shinji.addressbook.data";

    // base URI to interact with ContentProvider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Inner class that defines content for contact table
    // _ID
    // BaseColunms is autoim
    public static final class Contact implements BaseColumns {
        // give a table name
        public static final String TABLE_NAME = "contacts";
        // give all the columns a name
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_ZIP = "zip";

        // Uri for the contact Table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        //Create a uri for specific URI
        public static final Uri buildContactUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //Create a uri for specific contact using city
//        public static final Uri buildContactUriCity(String City){
//            return ContentUris.withAppendedId(CONTENT_URI, City);
//        }








    }


}
