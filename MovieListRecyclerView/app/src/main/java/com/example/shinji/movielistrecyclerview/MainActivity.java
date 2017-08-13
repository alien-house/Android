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
import java.util.List;

import static android.content.Intent.EXTRA_INDEX;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    MovieAdapter mAdapter;
    RecyclerView rvList;
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    private List booList = new ArrayList<>();
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

        rvList = (RecyclerView) findViewById(R.id.rv_movie);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setHasFixedSize(true);

        if(savedInstanceState != null) {
            System.out.println("=============onCreate---------------");
            movieList = savedInstanceState.getParcelableArrayList("LIST_INSTANCE_STATE");
        }else{
            createMovieList();
        }

        mAdapter = new MovieAdapter(movieList, this);
        rvList.setAdapter(mAdapter);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("------------onSaveInstanceState---------------");
        outState.putParcelableArrayList("LIST_INSTANCE_STATE", movieList);
        super.onSaveInstanceState(outState);
    }
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState!=null) {
//            System.out.println("=============onRestoreInstanceState---------------");
//            movieList = savedInstanceState.getParcelableArrayList("LIST_INSTANCE_STATE");
//            System.out.println("movieList:"+movieList.size());
//            for(Movie m : movieList){
//                System.out.println(m.isSelected());
//                m.setSelected(true);
//            }
////            mAdapter.setAllCheckBoxOn();
////            movieList = movieList2;
//
//            mAdapter.notifyDataSetChanged();
//        }
//
//    }

    public void selectAll(View view){
        System.out.println("^^_^^^^^^^^^^^^");
        for(Movie m : movieList){
            m.setSelected(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void clearAll(View view){
        for(Movie m : movieList){
            m.setSelected(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteMovie(View view){
        for(int i = movieList.size() - 1; i >= 0; i--){
            if(movieList.get(i).isSelected()){
                movieList.remove(i);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void deleteAllMovie(View view) {
        movieList.clear();
        mAdapter.notifyDataSetChanged();
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
