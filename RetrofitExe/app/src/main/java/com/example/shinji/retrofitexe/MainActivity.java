package com.example.shinji.retrofitexe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //QiitaApiの使用
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.86.148.94:8080")
                .baseUrl("http://api.indeed.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        QiitaApiService qiitaApiservice = retrofit.create(QiitaApiService.class);
        Call<List<QiitaGsonResponse>> call = qiitaApiservice.getData("2612264898312897","json",2,"web");
        try {
            call.enqueue(new Callback<List<QiitaGsonResponse>>() {
                @Override
                public void onResponse(Call<List<QiitaGsonResponse>> call, Response<List<QiitaGsonResponse>> response) {
                    List results = new ArrayList();
                    results = response.body().get(0).getResults();
                    Log.d("Qiita","====================");
                    for(Object item : results){
                        Log.d("Qiita:", (String) item);
                    }
//                    Log.d("Qiita",results);
                }
                @Override
                public void onFailure(Call<List<QiitaGsonResponse>> call, Throwable t) {

                    Log.d("Qiita",";;;;;;;;;;;;;;;;;;;;");
                    Log.d("Qiita", "error");
                    System.out.println(t);
                }

            });
        } catch (Exception e) {
            Log.d("Qiita", "レスポンスエラー");
        }


//        Controller controller = new Controller();
//        controller.start();
    }

}
