package com.example.shinji.jobsearchapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by shinji on 2017/08/04.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String detail_url = getIntent().getStringExtra("JOB_DETAIL_URL");
        System.out.println("---getSiteUrl--------------------------------");
        Log.e("URL****",detail_url);
        WebView wb = (WebView) findViewById(R.id.webview);
        wb.loadUrl(detail_url);
    }

}
