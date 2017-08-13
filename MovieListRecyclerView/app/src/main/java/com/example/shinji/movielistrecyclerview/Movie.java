package com.example.shinji.movielistrecyclerview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shinji on 2017/07/18.
 */

//1. Declare a privte boolean variable

public class Movie implements Parcelable {
    private String title = "";
    private String category = "";
    private int year = 0;
    private int imgID = 0;
    private int time = 0;
    private double rating = 0.0;
    private String director = "";
    private String cast = "";
    private String describe = "";
    private boolean isSelected;


    Movie(
            String title,
            String category,
            int year,
            int imgID,
            int time,
            double rating,
            String director,
            String cast,
            String describe
    ){
        this.title = title;
        this.category = category;
        this.year = year;
        this.imgID = imgID;
        this.time = time;
        this.rating = rating;
        this.director = director;
        this.cast = cast;
        this.describe = describe;
        this.isSelected = false;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        category = in.readString();
        year = in.readInt();
        imgID = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle(){
        return this.title;
    }
    public int getImgID(){
        return this.imgID;
    }
    public String getCategory(){
        return this.category;
    }
    public int getYear(){
        return this.year;
    }
    public int getTime(){
        return this.time;
    }
    public void setSelected(boolean selected){
        this.isSelected = selected;
    }
    public boolean isSelected(){
        return this.isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(category);
        parcel.writeInt(year);
        parcel.writeInt(imgID);

    }
}
