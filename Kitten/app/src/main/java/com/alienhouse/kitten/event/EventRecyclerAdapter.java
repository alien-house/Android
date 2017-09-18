package com.alienhouse.kitten.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienhouse.kitten.R;
import com.alienhouse.kitten.dashboard.GetImageTask;
import com.alienhouse.kitten.main.Job;
import com.alienhouse.kitten.util.FirebaseController;
import com.alienhouse.kitten.util.HeartAnimation;
import com.alienhouse.kitten.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shinji on 2017/09/04.
 */

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventHolder> {
    private int mNumberItems;
    private List<Event> eventList;
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

    public EventRecyclerAdapter(List<Event> eventList, ListItemClickListener onClickListner) {
        this.mNumberItems = eventList.size();
        this.eventList = eventList;
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
        View view = inflater.inflate(R.layout.event_list_item, parent, false);//this time, only textview
        EventHolder nh = new EventHolder(view);
        eventHolderList.add(nh);
        viewHolderCount++;
        return nh;
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, final int position) {

        final Event event = eventList.get(position);
        holder.event_title.setText(event.getTitle());
        holder.event_date.setText(event.getHoldDate());
        holder.event_venues.setText(event.getVenues());
        holder.event_address.setText(event.getAddress());
        ImageLoader imageLoader = ImageLoader.getInstance();
        if(!this.eventList.get(position).getImgUrl().matches("") || this.eventList.get(position).getImgUrl() == null){
            imageLoader.displayImage(this.eventList.get(position).getImgUrl(), holder.event_image);
        }else{
            holder.event_image.setImageResource(R.drawable.noimage);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.eventList.size();
    }

    class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView event_title;
        TextView event_date;
//        TextView event_end;
        TextView event_venues;
        TextView event_address;
        ImageView event_image;

        public EventHolder(View itemView) {
            super(itemView);
            event_title = (TextView) itemView.findViewById(R.id.event_title);
            event_date = (TextView) itemView.findViewById(R.id.event_date);
//            event_end = (TextView) itemView.findViewById(R.id.event_time);
            event_venues = (TextView) itemView.findViewById(R.id.event_venues);
            event_address = (TextView) itemView.findViewById(R.id.event_address);
            event_image = (ImageView) itemView.findViewById(R.id.event_image);
//
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickListner.onListItemClick(position);

        }
    }


}
