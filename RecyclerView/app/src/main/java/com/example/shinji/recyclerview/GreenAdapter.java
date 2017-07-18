package com.example.shinji.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shinji on 2017/07/17.
 */

public class GreenAdapter extends RecyclerView.Adapter<NumberViewHolder>{
    private int mNumberItems;
    private static int viewHolderCount;

    GreenAdapter(int numberOfItems){
        this.mNumberItems = numberOfItems;
        this.viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.number_list_item, parent, false);//this time, only textview
        NumberViewHolder nh = new NumberViewHolder(view);
        return nh;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }
}
