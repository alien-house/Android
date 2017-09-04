package com.example.shinji.kitten.main;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.airbnb.lottie.LottieAnimationView;
import com.example.shinji.kitten.R;
import com.example.shinji.kitten.util.FirebaseController;
import com.example.shinji.kitten.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by shinji on 2017/08/04.
 */

public class JobAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Job> jobList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseController firebaseController;
    private DatabaseReference favoriteRef;
    private User userData;

    public JobAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userData = firebaseController.getUserData(user);
        favoriteRef = database.getReference("favorite/" + userData.userID);
    }

    public void setJobList(ArrayList<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public int getCount() {
        return jobList.size();
    }

    @Override
    public Object getItem(int i) {
        return jobList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return jobList.get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final Job job = jobList.get(i);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.main_job_result_list, viewGroup, false);
            holder = new ViewHolder();

            holder.job_title = (TextView) view.findViewById(R.id.job_title);
            holder.job_company = (TextView) view.findViewById(R.id.job_company);
            holder.job_area = (TextView) view.findViewById(R.id.job_area);
            holder.job_description = (TextView) view.findViewById(R.id.job_description);
            holder.job_posttime = (TextView) view.findViewById(R.id.job_posttime);
            holder.animationView = (LottieAnimationView) view.findViewById(R.id.animationView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.job_title.setText(jobList.get(i).getTitle());
        holder.job_company.setText(jobList.get(i).getCompany());
        holder.job_area.setText(jobList.get(i).getArea());
        holder.job_posttime.setText(jobList.get(i).getPostedDate());
        /* ====================================================  */
        CharSequence source = Html.fromHtml(jobList.get(i).getDescription());
        holder.job_description.setText(source);
        holder.animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.clickon) {
                    holder.animationView.setProgress(0f);
                    holder.clickon = false;
                    Map<String, Object> postValues = job.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + job.getJobkey() + "/", postValues);
                    favoriteRef.child(job.getJobkey()).removeValue();

                } else {
                    holder.animationView.playAnimation();
                    holder.clickon = true;
                    Map<String, Object> postValues = job.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + job.getJobkey() + "/", postValues);
                    favoriteRef.updateChildren(childUpdates);

                }
            }
        });
//        view.setOnClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View views, int position, long id) {
//                String msg = position + "items";
//                Log.e("Msg:::",msg);
//
//            }
//        });
        return view;
    }

    class ViewHolder {
        TextView job_title;
        TextView job_company;
        TextView job_area;
        TextView job_description;
        TextView job_posttime;
        boolean clickon = false;
        LottieAnimationView animationView;
    }
}
