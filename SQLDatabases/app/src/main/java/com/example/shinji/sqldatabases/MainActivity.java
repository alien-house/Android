package com.example.shinji.sqldatabases;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * this class to ac
 * @author shinji
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseHandler dbHandler = new DatabaseHandler(this);
    Button btnAdd, btnUpdate, btnDelete, btnSortByTitle, btnSortByAuthor;
    EditText editTitle, editAuthor;

    private List<Book> booklist;
    private ListView listViewBooks;
    private BookAdapter bookAdapter;
    List<Integer> listID = new ArrayList<Integer>();
    List<String> listTitle = new ArrayList<String>();
    List<String> listAuthor = new ArrayList<String>();

    private Integer selectedID, selectedListPosition; //selected item ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(this, "DATABASE CREATED SUCCESFULLUY", Toast.LENGTH_LONG).show();
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSortByTitle = (Button) findViewById(R.id.btnSortByTitle);
        btnSortByAuthor = (Button) findViewById(R.id.btnSortByAuthor);
        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSortByTitle.setOnClickListener(this);
        btnSortByAuthor.setOnClickListener(this);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        listViewBooks = (ListView) findViewById(R.id.listview);
        getAllItem(0, true, "");

        //click
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedListPosition = position;
                selectedID = booklist.get(position).getId();
//                String txtTitle = booklist.get(position).getTitle();
//                String txtAuthor = booklist.get(position).getAuthor();
//                editTitle.setText(txtTitle);
//                editAuthor.setText(txtAuthor);

                Book b = null;
                b = dbHandler.readBook(selectedID);
                editTitle.setText(b.getTitle());
                editAuthor.setText(b.getAuthor());
//                Toast.makeText(getApplicationContext(), "BOOK READ SUCCESFULLY", Toast.LENGTH_LONG).show();
                Log.e("setOnItemClickListenerp", String.valueOf(position));
                Log.e("setOnItemClickListeners", String.valueOf(selectedID));
            }
        });

    }

    public void getAllItem(int startNum, boolean isFirst, String sort){

        if(!isFirst) {
            listID.clear();
            listTitle.clear();
            listAuthor.clear();
            booklist.clear();
        }
        //8 read from database into list
        booklist = dbHandler.getAllBooks(sort);

        for(int i = startNum; i < booklist.size(); i++){
            listID.add(i, booklist.get(i).getId());
            listTitle.add(i, booklist.get(i).getTitle());
            listAuthor.add(i, booklist.get(i).getAuthor());
        }
        if(isFirst){
            bookAdapter = new BookAdapter(this, listID, listTitle, listAuthor);
            listViewBooks.setAdapter(bookAdapter);
        }else{
            bookAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                if((editTitle.getText().toString().matches(""))||(editAuthor.getText().toString().matches(""))){
                    Toast.makeText(this, "You need to input title and author name", Toast.LENGTH_LONG).show();
                }else{
                    dbHandler.addBook(new Book(editTitle.getText().toString(), editAuthor.getText().toString()));
                    Toast.makeText(this, "DATABASE CREATED SUCCESFULLUY", Toast.LENGTH_LONG).show();
                    int maxNum = bookAdapter.getCount();
                    getAllItem(maxNum, false, "");
                }
                break;
            case R.id.btnUpdate:
                //for db
                Book book = new Book();
                book.setId(selectedID);
                book.setTitle(editTitle.getText().toString());
                book.setAuthor(editAuthor.getText().toString());
                dbHandler.updateBook(book);
                int rowAffected = dbHandler.updateBook(book);

                //for view
                listTitle.set(selectedListPosition, editTitle.getText().toString());
                listAuthor.set(selectedListPosition, editAuthor.getText().toString());
                bookAdapter.notifyDataSetChanged();
                Toast.makeText(this, "ROWS " + rowAffected + " are updated ", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnDelete:
                Book bookDelete = new Book();
                bookDelete.setId(selectedID);
                bookDelete.setTitle(editTitle.getText().toString());
                bookDelete.setAuthor(editAuthor.getText().toString());
                int rowAffectedDelete = dbHandler.deleteBook(bookDelete);
                Toast.makeText(this, "ROWS " + rowAffectedDelete + " are deleted ", Toast.LENGTH_SHORT).show();
                listID.remove(selectedListPosition);
                listTitle.remove(selectedListPosition);
                listAuthor.remove(selectedListPosition);
                bookAdapter.notifyDataSetChanged();
                break;
            case R.id.btnSortByTitle:
//                Toast.makeText(this, "Click btnSortByTitle", Toast.LENGTH_LONG).show();
                getAllItem(0, false, "title");
                break;
            case R.id.btnSortByAuthor:
//                Toast.makeText(this, "Click btnSortByAuthor", Toast.LENGTH_LONG).show();
                getAllItem(0, false, "author");
                break;
        }
        InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        view.clearFocus();
    }


}
