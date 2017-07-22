package com.example.shinji.movielistrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinji on 2017/07/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.TextViewHolder> {
    private int mNumberItems;
//    private ArrayList<Movie> movieList = new ArrayList<Movie>();
    private List<Movie> movieList;
    private static int viewHolderCount;
    private ArrayList<TextViewHolder> nhList = new ArrayList<TextViewHolder>();
    final private ListItemClickListener onClickListner;
    private int lastPosition = -1;

    public interface ListItemClickListener{
        void onListItemClick(int index);
    }

    MovieAdapter(List<Movie> movieList, ListItemClickListener listener){
        this.mNumberItems = movieList.size();
        this.movieList = movieList;
        this.viewHolderCount = 0;
        onClickListner = listener;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);//this time, only textview
        TextViewHolder nh = new TextViewHolder(view);
        int bg  = ColorUtils.getViewHolderBackgroundColorFromInstance(context,viewHolderCount);
        view.setBackgroundColor(bg);
        nhList.add(nh);
        System.out.println("***nh***:"+nh);
        viewHolderCount++;
        return nh;
    }

    @Override
    //表示されているリサイクルビューがアップデート時に呼ばれる。
    public void onBindViewHolder(TextViewHolder holder, final int position) {
        System.out.println("------------onBindViewHolder---------------");
        Movie movie = movieList.get(position);
        holder.chkBox.setChecked(movieList.get(position).isSelected());

        holder.bind(position);
//        public void onRemove(){
//            movieList.remove(position);
//            notifyItemRemoved(position);
////            notifyItemRangeChanged(position, contents2.size());
//        };
        holder.chkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                movieList.get(position).setSelected(true);
            }
        });
        setAnimation(holder.itemView, position);
//        slide(holder.itemView, position);
    }

    public void onRemove(int position) {
        movieList.remove(position);
        notifyItemRemoved(position);
        this.mNumberItems--;
    }

    @Override
    public int getItemCount() {
        return this.movieList.size();
    }

    public void setAllCheckBoxOn(){
        int i = 0;
        for (TextViewHolder tvh : nhList) {
            tvh.chkBox.setChecked(true);
            i++;
        }
    }
//    public void setAllCheckBoxOff(){
//        int i = 0;
//        for (TextViewHolder tvh : nhList) {
//            tvh.checkBoxOff();
//            setFadeAnimation(tvh.itemView);
//            i++;
//        }
//    }
//    public void removeItem(){
//        for(int i = 0; i < nhList.size(); i++){
//
//            if(nhList.get(i).getCheckState()){
//                nhList.remove(i);
//                movieList.remove(i);
//                notifyItemRemoved(i);
//                this.mNumberItems--;
////                tvh.
////                yourdatalist.remove(position);
////                notifyItemRemoved(position);
////                notifyItemRangeChanged(position,getItemCount());
//            }
//        }
//    }

    private void setAnimation(View viewToAnimate, int position){
        Context context = viewToAnimate.getContext();
        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }
    public void clockwise(View viewToAnimate,  int position){
        Animation animation = AnimationUtils.loadAnimation( viewToAnimate.getContext(),
                R.anim.myanimation);
        viewToAnimate.startAnimation(animation);
    }
    public void zoomAni(View viewToAnimate,  int position){
        Animation animation1 = AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                R.anim.clockwise);
        viewToAnimate.startAnimation(animation1);
    }
    public void fade(View viewToAnimate,  int position){
        Animation animation1 =
                AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                        R.anim.fade);
        viewToAnimate.startAnimation(animation1);
    }
    public void blink(View viewToAnimate,  int position){
        Animation animation1 =
                AnimationUtils.loadAnimation(viewToAnimate.getContext(),
                        R.anim.blink);
        ImageView imgView_Tmp = (ImageView) viewToAnimate.findViewById(R.id.tv_item_img);

        imgView_Tmp.startAnimation(animation1);
    }
    public void move(View viewToAnimate,  int position){
        Animation animation1 =
                AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.move);
        viewToAnimate.startAnimation(animation1);
    }
    public void slide(View viewToAnimate,  int position){
        Animation animation1 =
                AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide);
        viewToAnimate.startAnimation(animation1);
    }

    /*
      NumberViewHolder Class
     */

    class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvCategory;
        TextView tvYear;
        CheckBox chkBox;
        ImageView imgView;
//        View itemView;
        TextViewHolder(View itemView){
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_title);//中身の方
            tvCategory = (TextView) itemView.findViewById(R.id.tv_item_year);//中身の方
            tvYear = (TextView) itemView.findViewById(R.id.tv_item_category);//中身の方
            chkBox = (CheckBox) itemView.findViewById(R.id.movieCheckBox);
            imgView = (ImageView) itemView.findViewById(R.id.tv_item_img);
            itemView.setOnClickListener((View.OnClickListener) this);
//            this.itemView = itemView;
        }

        public void bind(int listIndex){
            tvTitle.setText(movieList.get(listIndex).getTitle());
            tvCategory.setText(movieList.get(listIndex).getCategory());
            tvYear.setText(String.valueOf(movieList.get(listIndex).getYear()));
            imgView.setImageResource(movieList.get(listIndex).getImgID());
//            tvTitle.setText("e");
        }

        public void checkBoxOn(){
            chkBox.setChecked(true);
        }

        public void checkBoxOff(){
            chkBox.setChecked(false);
        }

        public boolean getCheckState(){
            return chkBox.isChecked();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onClickListner.onListItemClick(position);
        }

    }
}
