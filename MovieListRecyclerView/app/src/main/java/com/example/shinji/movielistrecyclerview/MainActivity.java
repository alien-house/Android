package com.example.shinji.movielistrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.EXTRA_INDEX;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    MovieAdapter mAdapter;
    RecyclerView rvList;
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    private int NUM_LIST_ITEMS = 0;
    private static String EXTRA_DATA = "EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        createMovieList();
        rvList = (RecyclerView) findViewById(R.id.rv_movie);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(movieList, this);
        rvList.setAdapter(mAdapter);


        // Btn:All Clear
        Button btnAllClear = (Button)findViewById(R.id.btnAllClear);
        btnAllClear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllCheckBoxOff();
            }
        });

        // Btn:All Select
        Button btnAllSelect = (Button)findViewById(R.id.btnAllSelect);
        btnAllSelect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setAllCheckBoxOn();
            }
        });

        // Btn:All Delete
        Button btnAllDelete = (Button)findViewById(R.id.btnDelete);
        btnAllDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.removeItem();

            }
        });


//        btnAllClear.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                int itemPosition = position;
//                Intent i = new Intent(MainActivity.this, PageActivity.class);
//                i.putExtra(EXTRA_INDEX, itemPosition);
//                startActivity(i);
////                overridePendingTransition(0, 0);
//            }
//
//        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(int index) {
        int itemPosition = index;
        Intent i = new Intent(MainActivity.this, PageActivity.class);
        Movie mv_tmp = movieList.get(itemPosition);
        i.putExtra(EXTRA_DATA, mv_tmp);
        startActivity(i);
//        Toast.makeText(this, "Item # " + index + "is clicked", Toast.LENGTH_SHORT).show();
    }



    public void createMovieList(){
        movieList.add(new Movie("The Shawshank Redemption ", "Dorama", 1994,
                getImgID("shawshank"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("The Godfather","Dorama",1909,
                getImgID("godfather"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("The Godfather: Part II","Dorama",1909, getImgID("godfather2"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("The Dark Knight","Dorama",2008, getImgID("darkknight"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("12 Angry Men","Dorama",1957, getImgID("angry12"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("Schindler's List","Dorama",1909, getImgID("schindler"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("Pulp Fiction","Dorama",1909, getImgID("pulpfiction"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("The Lord of the Rings: The Return of the King","Dorama",2005, getImgID("lord"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("Forrest Gump","Dorama",1909, getImgID("forrest"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        movieList.add(new Movie("Fight Club","Dorama",1909, getImgID("fight"), 142, 9.3, "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."));

        NUM_LIST_ITEMS = movieList.size();
    }
    public int getImgID(String str){
        int ID = getResources().getIdentifier(str, "drawable", getPackageName());
        return ID;
    }

}
