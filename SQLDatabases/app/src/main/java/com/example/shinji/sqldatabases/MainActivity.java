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
    Button btnAdd,btnUpdate,btnDelete;
    EditText editTitle, editAuthor;

    private List<Book> booklist;
    private ListView listViewBooks;
    private BookAdapter bookAdapter;
    List<Integer> listID = new ArrayList<Integer>();
    List<String> listTitle = new ArrayList<String>();
    List<String> listAuthor = new ArrayList<String>();

    private Integer selectedID; //selected item ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(this, "DATABASE CREATED SUCCESFULLUY", Toast.LENGTH_LONG).show();
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        listViewBooks = (ListView) findViewById(R.id.listview);
        getAllItem(0, true);

        //click
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("setOnItemClickListener", String.valueOf(position));
                selectedID = booklist.get(position).getId();
                String txtTitle = booklist.get(position).getTitle();
                String txtAuthor = booklist.get(position).getAuthor();
                editTitle.setText(txtTitle);
                editAuthor.setText(txtAuthor);
            }
        });

    }

    public void getAllItem(int startNum, boolean isFirst){

        //8 read from database into list
        booklist = dbHandler.getAllBooks();

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
                }
                break;
            case R.id.btnUpdate:
                Toast.makeText(this, "UPDATED!", Toast.LENGTH_SHORT).show();
                int maxNum = bookAdapter.getCount();
                System.out.println(maxNum);
                getAllItem(maxNum, false);
                break;
            case R.id.btnDelete:
                break;
        }
        InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        view.clearFocus();
    }


}
