package com.example.shinji.flagquiz;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements FuncInterface {

    // TODO 1) 設定データのキーを設定する
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    public MainActivityFragment quizFragment;
    private boolean phoneDevice = true; // false=tablet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set defaultValues! これでデフォルトをセットできる
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        int ScreenSize = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        Log.e("ScreenSize:::",String.valueOf( ScreenSize));
        System.out.println("ScreenSize(.SCREENLAYOUT_SIZE_LARGE):::"+ String.valueOf(Configuration.SCREENLAYOUT_SIZE_LARGE));
        if(ScreenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE){
            phoneDevice = false;
        }

        if(phoneDevice){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


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

    private OnSharedPreferenceChangeListener preferenceChangeListener =
            new OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    MainActivityFragment quizFragment =
                            (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);
                    if(s.equals(CHOICES)){
                        quizFragment.updateGuessRows(sharedPreferences);
                        quizFragment.startQuiz();
                    }else if(s.equals(REGIONS)){
                        Log.e("REGIONS:::::","@@");
                        Set<String> regionsSet = sharedPreferences.getStringSet(REGIONS,null);
                        System.out.println(regionsSet.size());

                        if(regionsSet.size() == 0){
                            regionsSet.add("South_America");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putStringSet(REGIONS, regionsSet);
                            editor.apply();
                            Toast.makeText(getApplicationContext(),"Select at least one Resion",Toast.LENGTH_SHORT).show();
                        }
                        quizFragment.updateRegions(sharedPreferences);
                        quizFragment.startQuiz();
                    }
                    Toast.makeText(getApplicationContext(),"The quiz is reset",Toast.LENGTH_SHORT).show();
                }
            };

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

    @Override
    public void onReturnFnc() {
        quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);
        quizFragment.startQuiz();
    }
}
