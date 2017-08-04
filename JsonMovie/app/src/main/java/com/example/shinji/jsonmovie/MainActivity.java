package com.example.shinji.jsonmovie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private List<Movie> movieList1 = new ArrayList<>();
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private List<ArrayList<Integer>> MoviesChecked;
    // json array response url
//    private String urlJsonArry = "http://ciccc.localhost/android/moviedata.json";
    private String urlJsonArry = "http://192.168.0.59:8080/ciccc/android/moviedata.json";

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
        recyclerView = (RecyclerView) findViewById(R.id.rv_movie);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.d("error ", "in oncreate");
        makeJsonArrayRequest();
    }

    /**
     * Method to make json array request where response starts with [
     */
    private void makeJsonArrayRequest() {
        Log.d("HERE ", "in makeJsonArrayRequest!!!");
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse (JSONArray response) {
                        Log.d("HERE ", "in onResponse!!!");
                        Log.d(TAG, response.toString());
                        try {
                            Movie moviedata = null;
                            for (int i = 0; i < response.length(); i++) {
                                moviedata = new Movie();
                                JSONObject movie = (JSONObject) response.get(i);
                                String title = movie.getString("title");
                                String genre = movie.getString("genre");
                                String year = movie.getString("year");
                                String cast = movie.getString("cast");
                                moviedata.setTitle(title);
                                moviedata.setGenre(genre);
                                moviedata.setYear(year);
                                moviedata.setCast(cast);
                                movieList1.add(moviedata);
                            }
                            mAdapter = new MovieAdapter(movieList1);
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

//
//    private void loadJSON() {
//        //Create the instance of the Retrofit class and pass the api url(http://api2.mytrendin.com) inside the baseurl method.
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.59:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        APIservice service = retrofit.create(APIservice.class);
//        Call<List<Movie>> call = service.getMyJSON();
//        call.enqueue(new Callback<List<Movie>>() {
//            @Override
//            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
//
//                movieList = response.body();
//                Log.d("size  ", String.valueOf(movieList.size()));
//                for (int i = 0; i < movieList.size(); i++) {
//                    Log.d("movie = ", movieList.get(i).getTitle());
//                }
//                Movie movie = null;
//                for (int i = 0; i < movieList.size(); i++) {
//                    movie = new Movie();
//                    String title = movieList.get(i).getTitle();
//                    String genre = movieList.get(i).getGenre();
//                    String year = movieList.get(i).getYear();
//                    String cast = movieList.get(i).getCast();
//                    movie.setTitle(title);
//                    movie.setGenre(genre);
//                    movie.setYear(year);
//                    movie.setCast(cast);
//                    movieList1.add(movie);
//                }
//                mAdapter = new MovieAdapter(movieList1);
//                recyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<List<Movie>> call, Throwable t) {
//                Log.d("error : ", t.getMessage());
//            }
//        });
//    }








    public void deleteMovie(View view) {
        for (int i = 0; i < movieList1.size(); i++) {
            if (movieList1.get(i).isSelected()) {
                movieList1.remove(i);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void selectAll(View view) {
        for (Movie m : movieList1) {
            m.setSelected(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void clearAll(View view) {
        for (Movie m : movieList1) {
            m.setSelected(false);
            mAdapter.notifyDataSetChanged();
        }
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
}
