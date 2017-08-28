package com.example.shinji.kitten.main;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.shinji.kitten.R;

/**
 * Created by shinji on 2017/08/04.
 */

public class JobAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Job> jobList;

    public JobAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (view == null) {
            view = layoutInflater.inflate(R.layout.main_job_result_list, viewGroup, false);
            holder = new ViewHolder();

            holder.job_title = (TextView) view.findViewById(R.id.job_title);
            holder.job_company = (TextView) view.findViewById(R.id.job_company);
            holder.job_area = (TextView) view.findViewById(R.id.job_area);
            holder.job_description = (TextView) view.findViewById(R.id.job_description);
            holder.job_posttime = (TextView) view.findViewById(R.id.job_posttime);
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
    }
}
