package com.example.shinji.retrofitexe;

/**
 * Created by shinji on 2017/07/28.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GerritAPI {

    @GET("/ciccc/android/moviedata.json")
    Call<List<Change>> loadChanges(@Query("q") String status);
}