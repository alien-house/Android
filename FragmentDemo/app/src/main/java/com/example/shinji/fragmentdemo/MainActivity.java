package com.example.shinji.fragmentdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // it gives you the phone's orientation potrait or landscape
        Configuration config = getResources().getConfiguration();//横か盾かを取得。

            //create a instance for fragmentmaneger
            FragmentManager framentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = framentManager.beginTransaction();

        //chack the orientation
        //act accordingly
        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            LM_fragment lmfragment = new LM_fragment();
            fragmentTransaction.replace(android.R.id.content, lmfragment);
        }else{
            //create an instance for potarait
            PM_fragment pmfragment = new PM_fragment();
            fragmentTransaction.replace(android.R.id.content, pmfragment);
        }
        fragmentTransaction.commit();
    }

}
