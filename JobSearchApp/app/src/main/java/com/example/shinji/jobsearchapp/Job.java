package com.example.shinji.jobsearchapp;

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
    Job(String title, String url, String company, String description, String postedDate, String area){
        this.id = hashCode();
        this.title = title;
        this.url = url;
        this.company = company;
        this.description = description;
        this.postedDate = postedDate;
        this.area = "@ " + area;
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
}
