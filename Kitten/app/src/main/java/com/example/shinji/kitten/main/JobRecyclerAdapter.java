package com.example.shinji.kitten.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shinji.kitten.R;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.HeartAnimation;
import com.example.shinji.kitten.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shinji on 2017/09/04.
 */

public class JobRecyclerAdapter extends RecyclerView.Adapter<JobRecyclerAdapter.JobHolder> {
    private int mNumberItems;
    private List<Job> jobList;
    private ArrayList<JobHolder> JobHolderList = new ArrayList<JobHolder>();
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

    public JobRecyclerAdapter(List<Job> jobList, ListItemClickListener onClickListner) {
        this.mNumberItems = jobList.size();
        this.jobList = jobList;
        this.viewHolderCount = 0;
        this.onClickListner = onClickListner;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        firebaseController = firebaseController.getInstance();
        userData = firebaseController.getUserData();
        System.out.println("JobRecyclerAdapter:" + userData.username);
        favoriteRef = database.getReference("favorite/" + userData.userID);
    }
    @Override
    public JobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main_job_result_list_item, parent, false);//this time, only textview
        JobHolder nh = new JobHolder(view);
        JobHolderList.add(nh);
        viewHolderCount++;
        return nh;
    }

    @Override
    public void onBindViewHolder(final JobHolder holder, final int position) {

        final Job job = jobList.get(position);
//        holder.chkBox.setChecked(jobList.get(position).isSelected());
        holder.job_title.setText(job.getTitle());
        holder.job_company.setText(job.getCompany());
        holder.job_area.setText(job.getArea());
        holder.job_posttime.setText(job.getPostedDate());
        CharSequence source = Html.fromHtml(job.getDescription());
        holder.job_description.setText(source);
        if(job.isFavd()){
            holder.animationView.setProgress(1f);
        }
        holder.animationView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                holder.animationView.setOn();
                if(jobList.get(position).isFavd()) {
                    holder.animationView.setProgress(0f);
                    jobList.get(position).setFav(false);
                    Map<String, Object> postValues = job.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + job.getJobkey() + "/", postValues);
                    favoriteRef.child(job.getJobkey()).removeValue();
                    Log.e("animationView:", String.valueOf(jobList.get(position).isFavd()));
                }else{
                    holder.animationView.playAnimation();
                    jobList.get(position).setFav(true);
                    Map<String, Object> postValues = job.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + job.getJobkey() + "/", postValues);
                    favoriteRef.updateChildren(childUpdates);
                    Log.e("animationView:", String.valueOf(jobList.get(position).isFavd()));
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.jobList.size();
    }

    class JobHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView job_title;
        TextView job_company;
        TextView job_area;
        TextView job_description;
        TextView job_posttime;
        HeartAnimation animationView;

        public JobHolder(View itemView) {
            super(itemView);
            job_title = (TextView) itemView.findViewById(R.id.job_title);
            job_company = (TextView) itemView.findViewById(R.id.job_company);
            job_area = (TextView) itemView.findViewById(R.id.job_area);
            job_description = (TextView) itemView.findViewById(R.id.job_description);
            job_posttime = (TextView) itemView.findViewById(R.id.job_posttime);
            animationView = (HeartAnimation) itemView.findViewById(R.id.animationView);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickListner.onListItemClick(position);
        }
    }


}
