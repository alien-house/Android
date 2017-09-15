package com.alienhouse.kitten.onlinecourse;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by shinji on 2017/08/01.
 */
@IgnoreExtraProperties
public class Onlinecourse {
    public String name;
    public String url;
    public String desc;
    public String image;

    public Onlinecourse() {
        // Default constructor required for calls to DataSnapshot.getValue(Onlinecourse.class)
    }
    public Onlinecourse(String name, String url, String desc, String image){
        this.name = name;
        this.url = url;
        this.desc = desc;
        this.image = image;
    }
    public String getName(){
        return this.name;
    }
    public String getUrl(){ return this.url;}
    public String getDescription(){
        return this.desc;
    }
    public String getImgUrl(){
        return this.image;
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
