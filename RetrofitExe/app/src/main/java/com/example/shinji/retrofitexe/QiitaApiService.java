package com.example.shinji.retrofitexe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by shinji on 2017/08/03.
 */

public interface QiitaApiService {
//    @GET("/ciccc/android/moviedata.json")
//    @GET("/ads/apisearch")
    @GET("items")
    Call<List<QiitaGsonResponse>> getData(
//            @Query ("page") String page,
//            @Query ("per_page") String per_page,
//            @Query ("query") String query

            @Query ("publisher") String publisher,
            @Query ("format") String format,
            @Query ("v") int v,
            @Query ("q") String query,
            @Query ("l") String location
            ,@Query ("userip") String userip
            ,@Query ("useragent") String useragent
    );



}