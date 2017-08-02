package com.example.shinji.flagquiz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // TODO 1) 設定データのキーを設定する
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set defaultValues! これでデフォルトをセットできる
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        Configuration config = getResources().getConfiguration();//横か盾かを取得。
//
//        FragmentManager framentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = framentManager.beginTransaction();
//        MainActivityFragment lmfragment = new MainActivityFragment();
//        fragmentTransaction.replace(android.R.id.content, lmfragment);
//        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //// TODO 2) クイズフラグメントインスタンスを作る
        MainActivityFragment quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

        //TODO 3) 選択数について：ユーザー選択オプションをアップデートする
        quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));

        //TODO 4) 出題国について：ユーザー選択オプションをアップデートする
        quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));

        //TODO 10) クイズをスタートする
        quizFragment.startQuiz();
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
            Intent preferencesIntent = new Intent(MainActivity.this, PreferenceActivity.class);
            startActivity(preferencesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
