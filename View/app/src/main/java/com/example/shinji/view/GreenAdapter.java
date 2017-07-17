package com.example.shinji.view;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/17.
 */

public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder>{

    private int mNumberListItems;
    private  int e;

    public GreenAdapter(int mNumberListItems){
        this.mNumberListItems = mNumberListItems;
    }

    //its for text view
    class NumberViewHolder extends RecyclerView.ViewHolder{
        TextView ListItemNumberView;
        NumberViewHolder(View itemView){
            super(itemView);
            ListItemNumberView = (TextView) itemView.findViewById(R.id.tv_listitem);
        }
        public void bind(int ListIndex){
            ListItemNumberView.setText(String.valueOf(ListIndex));
        }
    }

    // return for inner class: its for layout
    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.number_list_item1, parent, false);//this time, only textview
        NumberViewHolder viewHolder = new NumberViewHolder(view);
//        int bg  = ColorUtils.getViewHolderBackgroundColorFromInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mNumberListItems;
    }
}
