package com.example.shinji.asgrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by shinji on 2017/07/27.
 */

public class PageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        Recipe recipe = getIntent().getParcelableExtra("RECIPEDATA");

        WebView wb = (WebView) findViewById(R.id.webview);
        wb.loadUrl(recipe.getSiteUrl());
        System.out.println("---getSiteUrl--------------------------------");
        System.out.println(recipe.getSiteUrl());
//        wb.loadUrl("https://www.example.com");
    }


}
