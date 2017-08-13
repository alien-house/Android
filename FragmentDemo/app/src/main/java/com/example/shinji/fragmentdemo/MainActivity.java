package com.example.shinji.fragmentdemo;

import android.app.*;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // it gives you the phone's orientation potrait or landscape
        Configuration config = getResources().getConfiguration();//横か盾かを取得。

        //create a instance for fragmentmaneger
        FragmentManager framentManager = getFragmentManager();
        fragmentTransaction = framentManager.beginTransaction();

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


        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment df = new DialogFragment();
                //show a dialog box
                df.show(fragmentTransaction, "dialog fragment");
            }
        });
//
//        alertBtn.setOnCickLinstener(){
//            @Override
//            public void onClick(View v){
//                AlertDialogFragment af = new AlertDialogFragment();
//                //show a dialog box
//                af.show(framentManager, "alert dialog fragment");
//            }
//        }




    }


}
