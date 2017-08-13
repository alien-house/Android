package com.example.shinji.view;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/12.
 */

public class TextImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater l;
    //今回はアダプタの中で詳細なテキストとか、画像とかを入れた。
    // references to our images
    private Integer[] mThumbIds = {
        R.drawable.sample_0, R.drawable.sample_1,
        R.drawable.sample_2, R.drawable.sample_3
    };
    private String[] mTexts = {
        "Mendokusai", "Mouiya",
        "Kaerouka", "Demoyaruka"
    };
    public TextImageAdapter(Context mContext){
        this.mContext = mContext;
        l = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    //各行を表示しようとした時に呼ばれる
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (convertView == null) {
          //LayoutInflater xmlで定義したレイアウトを適応できる。
            convertView = l.inflate(R.layout.grid_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.label);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.logo);
        textView.setText(mTexts[position]);
        imageView.setImageResource(mThumbIds[position]);
        return convertView;

//        ImageView imageView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            System.out.println("mContext"+mContext);
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//        imageView.setImageResource(mThumbIds[position]);
//        return imageView;
    }
}
