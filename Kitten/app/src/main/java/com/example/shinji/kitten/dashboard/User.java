package com.example.shinji.kitten.dashboard;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shinji on 2017/08/29.
 */

public class User {
    public String userID,username,role,devStatus,email,bio,location,url = "";
    public static String USER_COUNTRY;
    //,comapny,lindkedin,twitter,github,facebook,dribbble

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String userID, String username, String devStatus, String email, String bio, String location, String url) {
        this.userID = userID;
        this.username = username;
        this.devStatus = devStatus;
        this.email = email;
        this.bio = bio;
        this.location = location;
        this.url = url;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("userID", userID);
//        result.put("username", username);
        result.put("devStatus", devStatus);
//        result.put("email", email);
        System.out.println("^^^^^bio^^^^^^");
        System.out.println(bio);
        result.put("bio", bio);
        result.put("location", location);
        result.put("url", url);

        return result;
    }
}
