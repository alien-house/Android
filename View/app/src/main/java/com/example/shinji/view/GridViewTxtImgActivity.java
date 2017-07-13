package com.example.shinji.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

/**
 * Created by shinji on 2017/07/12.
 */

public class GridViewTxtImgActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridviewtxtimg);//グリッドレイアウトが呼ばれる
        GridView gridview = (GridView) findViewById(R.id.gv1);//一つのグリッドを取ってくる。テンプレとして。
        gridview.setAdapter(new TextImageAdapter(this));//アダブタをセット。でどんなアダプタかと言うと。。。
    }
}
