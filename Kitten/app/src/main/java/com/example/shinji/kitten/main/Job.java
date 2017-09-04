package com.example.shinji.kitten.main;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shinji on 2017/08/01.
 */

public class Job {
    private int id;
    private String title;
    private String url;
    private String company;
    private String description;
    private String postedDate;
    private String area;
    private String jobkey;
    Job(String title, String url, String company, String description, String postedDate, String area, String jobkey){
        this.id = hashCode();
        this.title = title;
        this.url = url;
        this.company = company;
        this.description = description;
        this.postedDate = postedDate;
        this.area = "@ " + area;
        this.jobkey = jobkey;
    }
    public int getID(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getUrl(){ return this.url;}
    public String getCompany(){
        return this.company;
    }
    public String getDescription(){
        return this.description;
    }
    public String getPostedDate(){
        return this.postedDate;
    }
    public String getArea(){
        return this.area;
    }
    public String getJobkey(){ return this.jobkey; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("url", url);
        result.put("company", company);
        result.put("description", description);
        result.put("area", area);
        result.put("jobkey", jobkey);

        return result;
    }
}
