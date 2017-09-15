package com.alienhouse.kitten.util;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shinji on 2017/08/29.
 */

public class User {
    public String userID,username,role,devStatus,email,bio,location,url,photourl,country = "";
    public double lat,lon = 0.0;
    //,comapny,lindkedin,twitter,github,facebook,dribbble
    public static String USER_COUNTRY = "";
    public static String USER_LOCATION = "";
    public static double USER_LAT = 0.0;
    public static double USER_LON = 0.0;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String userID, String username, String devStatus, String email, String bio, String location, String url, String photourl, String country) {
        this.userID = userID;
        this.username = username;
        this.devStatus = devStatus;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.url = url;
        this.photourl = photourl;
        this.country = country;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("devStatus", devStatus);
        System.out.println("^^^^^bio^^^^^^");
        System.out.println(country);
        result.put("bio", bio);
        result.put("location", location);
        result.put("url", url);
        result.put("country", country);
        result.put("lat", lat);
        result.put("lon", lon);

        return result;
    }
}
