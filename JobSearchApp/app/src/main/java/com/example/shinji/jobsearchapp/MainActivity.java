package com.example.shinji.jobsearchapp;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button btnSearch;
    final String URL_INDEED = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent preferencesIntent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(preferencesIntent);
            }
        });

        loadJSON();

    }


    // http://api.indeed.com/ads/apisearch?publisher=2612264898312897&q=web+developer&l=vancouver%2C+bc&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=ca&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0(Firefox)&v=2&format=json
    private void loadJSON() {
        //Create the instance of the Retrofit class and pass the api url(http://api2.mytrendin.com) inside the baseurl method.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.indeed.com/ads/apisearch?publisher=2612264898312897&q=web+developer&l=vancouver%2C+bc&sort=&radius=&st=&jt=&start=&limit=&fromage=&filter=&latlong=1&co=ca&chnl=&userip=1.2.3.4&useragent=Mozilla/%2F4.0(Firefox)&v=2&format=json")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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
//
    }






}
