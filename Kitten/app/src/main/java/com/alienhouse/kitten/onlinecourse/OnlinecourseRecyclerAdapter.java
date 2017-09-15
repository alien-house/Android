package com.alienhouse.kitten.onlinecourse;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienhouse.kitten.R;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shinji on 2017/09/04.
 */

public class OnlinecourseRecyclerAdapter extends RecyclerView.Adapter<OnlinecourseRecyclerAdapter.EventHolder> {
    private int mNumberItems;
    private List<Onlinecourse> onlinecourseList;
    private ArrayList<EventHolder> eventHolderList = new ArrayList<EventHolder>();
    private static int viewHolderCount;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseController firebaseController;
    private DatabaseReference favoriteRef;
    private User userData;

    final private ListItemClickListener onClickListner;
    public interface ListItemClickListener{
        void onListItemClick(int index);
    }

    public OnlinecourseRecyclerAdapter(List<Onlinecourse> onlinecourseList, ListItemClickListener onClickListner) {
        this.mNumberItems = onlinecourseList.size();
        this.onlinecourseList = onlinecourseList;
        this.viewHolderCount = 0;
        this.onClickListner = onClickListner;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        firebaseController.getInstance();
        userData = firebaseController.getUserData();
//        favoriteRef = database.getReference("favorite/" + userData.userID);
    }
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.onlinecourse_list_item, parent, false);//this time, only textview
        EventHolder nh = new EventHolder(view);
        eventHolderList.add(nh);
        viewHolderCount++;
        return nh;
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, final int position) {

        final Onlinecourse onlinecourse = onlinecourseList.get(position);
//        holder.chkBox.setChecked(jobList.get(position).isSelected());
        holder.ol_title.setText(onlinecourse.getName());
        holder.ol_desc.setText(onlinecourse.getDescription());
//        holder.event_end.setText(onlinecourse.getHoldEnd());
//        CharSequence source = Html.fromHtml(job.getDescription());
//        holder.event_image.setImageBitmap();
        ImageLoader imageLoader = ImageLoader.getInstance();
        if(!this.onlinecourseList.get(position).getImgUrl().matches("") || this.onlinecourseList.get(position).getImgUrl() == null){
//            GetImageTask myTask = new GetImageTask(holder.event_image);
//            myTask.execute(this.onlinecourseList.get(position).getImgUrl());
            imageLoader.displayImage(this.onlinecourseList.get(position).getImgUrl(), holder.ol_image);
        }else{
            holder.ol_image.setImageResource(R.drawable.noimage);
        }
        System.out.println("一回だけやろうな！オンライン========"+position);


//        if(!holder.imgLoaded){
//        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.onlinecourseList.size();
    }

    class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ol_title;
        TextView ol_desc;
        ImageView ol_image;

        public EventHolder(View itemView) {
            super(itemView);
            ol_title = (TextView) itemView.findViewById(R.id.ol_title);
            ol_desc = (TextView) itemView.findViewById(R.id.ol_desc);
            ol_image = (ImageView) itemView.findViewById(R.id.ol_image);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickListner.onListItemClick(position);

        }
    }


}
