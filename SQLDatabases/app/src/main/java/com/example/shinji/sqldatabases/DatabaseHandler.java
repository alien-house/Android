package com.example.shinji.sqldatabases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shinji on 2017/08/14.
 * @see SQLiteOpenHelper
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    //Creating a database
    public static String DATABASE_NAME = "Bookdb";
    //current version of database
    private static int DATABASE_VERSION = 1;

    //need to create constructor
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //4. Create a Table Name
    private static final String TABLE_NAME = "book";

    //5.All keys ised om table with their
    private static final String KEY_ID = "bookid";
    private static final String KEY_NAME = "title";
    private static final String KEY_AUTHOR = "author";

    //6. Create a query statement for create a table in database
    private static final String CREATE_TABLE_BOOKS =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    KEY_ID + " INTEGER " + "PRIMARY KEY AUTOINCREMENT," +
                    KEY_NAME + " TEXT," +
                    KEY_AUTHOR + " TEXT" +
                    ");";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //7. create a table book
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOKS);
//        Toast.makeText(this, "DATABASE SCHEMA IS Create", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + CREATE_TABLE_BOOKS);
        this.onCreate(sqLiteDatabase);
    }

    public void addBook(Book book){
        //get the reference to wtiteable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //Create Contents & Value pair
        ContentValues value = new ContentValues();
        value.put(KEY_NAME, book.getTitle());
        value.put(KEY_AUTHOR, book.getAuthor());
        //insert the data into table
        db.insert(TABLE_NAME, null, value);
        //close the database connection
        db.close();
    }

    public int updateBook(Book book){
        //1. Open datebase wtiteable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //2. create ContentValue (key&value pair)
        ContentValues value = new ContentValues();
        value.put(KEY_NAME, book.getTitle());
        value.put(KEY_AUTHOR, book.getAuthor());
        //3. updaing a row using db.update()
        int result = db.update(TABLE_NAME, value, KEY_ID + " = ? ",
                new String[]{String.valueOf(book.getId())});
        db.close();
        return result;
    }

    public int deleteBook(Book book){
        //1. Open datebase wtiteable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //3. updaing a row using db.update()
        int result = db.delete(TABLE_NAME, KEY_ID + " = ? ",
                new String[]{String.valueOf(book.getId())});
        db.close();
        return result;
    }


    public Book readBook(int selectedID){
        // open the database of the application context
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID   + " = " + selectedID;
        // read the book with "id" from the database
        // get book query
        Cursor cursor = db.rawQuery(query,null);

        // if results !=null, parse the first one
        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book();
        book.setId(Integer.parseInt(cursor.getString(0)));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));

        db.close();
        return book;
    }

    public List<Book> getAllBooks(String sort){
        List<Book> books = new LinkedList<Book>();
        //9. Create a select query
        String query = "SELECT * FROM " + TABLE_NAME;
        if(!sort.equals("")){
            query += " ORDER BY "+ sort + " ASC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // rawquery directly accepts SQL statement as its input and it returns cursor object
        // which will point to one row of query result

        //11. go over result and buid book object and add it to the list
        Book book = null;
        if(cursor.moveToFirst()){
            do{
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                books.add(book);
            }while(cursor.moveToNext());
        }
        return books;
    }


}




