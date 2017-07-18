package com.example.shinji.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/17.
 */

//holderがいっぱい?
public class NumberViewHolder extends RecyclerView.ViewHolder{
    TextView listItemNumberView;
    NumberViewHolder(View itemView){
        super(itemView);
        listItemNumberView = (TextView) itemView.findViewById(R.id.tv_item_number);//中身の方
    }
    public void bind(int listIndex){
        listItemNumberView.setText(String.valueOf(listIndex));

    }
}
