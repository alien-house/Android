package com.example.shinji.asgrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shinji on 2017/07/24.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>  {
    final private ListItemClickListener onClickListner;
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    public interface ListItemClickListener{
        void onListItemClick(int index);
        void onListItemLongClick(int index);
    }

    RecipeAdapter(ArrayList<Recipe> recipeList, ListItemClickListener listener){
        this.recipeList = recipeList;
        onClickListner = listener;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_list_data, parent, false);//this time, only textview
        RecipeViewHolder viewholder = new RecipeViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, final int position) {
        Recipe recipe = recipeList.get(position);
        holder.chkBox.setChecked(recipeList.get(position).isSelected());
        holder.tvName.setText(recipe.getName());
        holder.tvDesc.setText(recipe.getDesc());
        holder.chkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                recipeList.get(position).setSelected(true);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.recipeList.size();
    }

    /*
      NumberViewHolder Class
     */

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView tvName;
        TextView tvDesc;
        CheckBox chkBox;
        Boolean myCheck;
        RecipeViewHolder(View itemView){
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.recipe_item_title);
            tvDesc = (TextView) itemView.findViewById(R.id.recipe_item_desc);
            chkBox = (CheckBox) itemView.findViewById(R.id.recipe_item_chkbox);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onClickListner.onListItemClick(position);
        }
        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            System.out.println("**** when ididididididid ***");
            onClickListner.onListItemLongClick(position);
            return true;
        }
    }
}
