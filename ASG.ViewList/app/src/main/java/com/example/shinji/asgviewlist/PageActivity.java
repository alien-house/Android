package com.example.shinji.asgviewlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/12.
 */

public class PageActivity extends AppCompatActivity {
  private static String EXTRA_INDEX = "SUPER_INDEX";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.page_layout);
    int[] s = getIntent().getIntArrayExtra(EXTRA_INDEX);


    System.out.println("========================");
    if (s != null) {
      System.out.println(s);
    }else{
      System.out.println("nullダヨーーーーーーー");
    }

//        Log.d("de",s);

//    TextView txt = (TextView) findViewById(R.id.textView);
//    txt.setText(s);
//
//    Button button = (Button) findViewById(R.id.button);
//    button.setOnClickListener( new View.OnClickListener() {
//
//      @Override
//      public void onClick(View v) {
//        startActivity(new Intent(SecondActivity.this, MainActivity.class));
//      }
//    });
  }


}
