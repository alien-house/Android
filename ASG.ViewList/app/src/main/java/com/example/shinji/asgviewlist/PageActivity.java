package com.example.shinji.asgviewlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/12.
 */

public class PageActivity extends AppCompatActivity {
  private static String EXTRA_INDEX = "SUPER_INDEX";
  private static String listText[];
  private int currentNum;
  private int maxNum;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.page_layout);
    currentNum = getIntent().getIntExtra(EXTRA_INDEX,0);
    listText = getResources().getStringArray(R.array.lists_array);
    maxNum = listText.length - 1;
    setTitle(listText[currentNum]);
    System.out.println("========================");
    System.out.println(currentNum);
    if (currentNum < 0) {
      System.out.println("Error");
    }
    TextView txt = (TextView) findViewById(R.id.txt);
    MovementMethod movementmethod = LinkMovementMethod.getInstance();
    txt.setMovementMethod(movementmethod);

    String txtname = "page_contents"+currentNum;
    System.out.println(txtname);
    int resId = getResources().getIdentifier(txtname, "string", getPackageName());
    String txtRes = getResources().getString(resId);
    CharSequence spanned = Html.fromHtml(txtRes);
    txt.setText(spanned);

    ImageView img = (ImageView) findViewById(R.id.img);
    String imgname = "layout_img"+(currentNum + 1);
    int id = getResources().getIdentifier(imgname, "drawable", getPackageName());
    img.setImageResource(id);

    Button btn_pre = (Button) findViewById(R.id.btn_pre);
    btn_pre.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        transitionToPage(-1);
      }
    });

    Button btn_next = (Button) findViewById(R.id.btn_next);
    btn_next.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        transitionToPage(1);
      }
    });

  }
//  @Override
//  public void finish() {
//    super.finish();
//
//    overridePendingTransition(0, 0);
//  }

  public void transitionToPage(int num){
    Intent i = null;
    switch (num){
      case 1:
        if(currentNum >= maxNum){
          i = new Intent(PageActivity.this, MainActivity.class);
        }else{
          i = new Intent(PageActivity.this, PageActivity.class);
          i.putExtra(EXTRA_INDEX, currentNum+(num));
        }
        break;
      case -1:
        if(currentNum <= 0){
          i = new Intent(PageActivity.this, MainActivity.class);
        }else{
          i = new Intent(PageActivity.this, PageActivity.class);
          i.putExtra(EXTRA_INDEX, currentNum+(num));
        }
        break;
    }
    startActivity(i);
  }

}
