package com.example.shinji.movielistrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/20.
 */

public class PageActivity extends AppCompatActivity {
    private static String EXTRA_DATA = "EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_layout);
        Movie movie = getIntent().getParcelableExtra(EXTRA_DATA);
        System.out.println("========================");
        System.out.println(movie.getTitle());
        setTitle(movie.getTitle());

        TextView tvTitle = (TextView) findViewById(R.id.detail_title);
        TextView tvYear = (TextView) findViewById(R.id.detail_year);
        TextView tvTime = (TextView) findViewById(R.id.detail_time);
        TextView tvDir = (TextView) findViewById(R.id.detail_director);
        TextView tvCast = (TextView) findViewById(R.id.detail_cast);
        ImageView coverImgView = (ImageView) findViewById(R.id.detail_cover);
        ImageView ratingImgView = (ImageView) findViewById(R.id.detail_rating);
        TextView tvRating = (TextView) findViewById(R.id.detail_rating_txt);
        TextView tvDis = (TextView) findViewById(R.id.detail_txt);

        tvTitle.setText(movie.getTitle());
        coverImgView.setImageResource(movie.getImgID());

    }

}
