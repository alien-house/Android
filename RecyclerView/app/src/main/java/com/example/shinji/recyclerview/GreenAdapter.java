package com.example.shinji.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shinji on 2017/07/17.
 */

public class GreenAdapter extends RecyclerView.Adapter<GreenAdapter.NumberViewHolder>{
    private static final String TAG = GreenAdapter.class.getSimpleName();
    private int mNumberItems;
    private static int viewHolderCount;
    // we are creating interface
    final private ListItemClickListener onClickListner;
    public interface ListItemClickListener{
        void onListItemClick(int index);
    }

    GreenAdapter(int numberOfItems, ListItemClickListener listener){
        this.mNumberItems = numberOfItems;
        onClickListner = listener;
        this.viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.number_list_item, parent, false);//this time, only textview
        NumberViewHolder nh = new NumberViewHolder(view);
        System.out.println(nh);
        int bg  = ColorUtils.getViewHolderBackgroundColorFromInstance(context,viewHolderCount);
        view.setBackgroundColor(bg);
        viewHolderCount++;
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


    /*
      NumberViewHolder Class
     */

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listItemNumberView;
        TextView indexView;
        NumberViewHolder(View itemView){
            super(itemView);
            listItemNumberView = (TextView) itemView.findViewById(R.id.tv_item_number);//中身の方
            indexView = (TextView) itemView.findViewById(R.id.tv_view_holder_instance);//中身の方
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        public void bind(int listIndex){
            listItemNumberView.setText(String.valueOf(listIndex));
            indexView.setText("ViewHolder index: " + String.valueOf(viewHolderCount));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onClickListner.onListItemClick(position);
        }
    }



}
