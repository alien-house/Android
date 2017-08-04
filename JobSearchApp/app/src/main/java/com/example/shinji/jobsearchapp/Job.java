package com.example.shinji.jobsearchapp;

/**
 * Created by shinji on 2017/08/01.
 */

public class Job {
    private String title;
    private String url;
    private String company;
    private String description;
    private String postedDate;
    Job(String title, String url, String company, String description, String postedDate){
        this.title = title;
        this.url = url;
        this.company = company;
        this.description = description;
        this.postedDate = postedDate;
    }
    public String getTitle(){
        return this.title;
    }
    public String getUrl(){
        return this.company;
    }
    public String getCompany(){
        return this.company;
    }
    public String getDescription(){
        return this.description;
    }
    public String getPostedDate(){
        return this.postedDate;
    }
}
