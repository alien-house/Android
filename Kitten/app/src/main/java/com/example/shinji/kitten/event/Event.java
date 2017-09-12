package com.example.shinji.kitten.event;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shinji on 2017/08/01.
 */

public class Event {
    private int id;
    private String title;
    private String url;
    private String organizer;
    private String description;
    private String holdDate;
    private String holdTime;
    private String venues;
    private String address;

    public Event(int id, String title, String url, String organizer, String description, String holdDate, String holdTime, String venues, String address){
        this.id = id;
        this.title = title;
        this.url = url;
        this.organizer = organizer;
        this.description = description;
        this.holdDate = holdDate;
        this.holdTime = holdTime;
        this.venues = venues;
        this.address = address;
    }
    public int getID(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getUrl(){ return this.url;}
    public String getOrganizer(){
        return this.organizer;
    }
    public String getDescription(){
        return this.description;
    }
    public String getHoldDate(){
        return this.holdDate;
    }
    public String getHoldTime(){
        return this.holdTime;
    }
    public String getVenues(){
        return this.venues;
    }
    public String getAddress(){
        return this.address;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("id", id);
//        result.put("title", title);
//        result.put("url", url);
//        result.put("company", company);
//        result.put("description", description);
//        result.put("area", area);
//        result.put("jobkey", jobkey);
//
//        return result;
//    }
}
