package com.example.shinji.jsonmovie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CICCC-CIRAC on 2017-07-17.
 */

public class Movie implements Serializable {
    private String title;
    private String  genre, year,cast;
    private int thumbnail;
    private boolean isSelected; //boolean varaiable for a movie

//
//    public Movie(String title, String genre, String year, String cast) {
//        this.title = title;
//        this.genre = genre;
//        this.year = year;
//        this.cast = cast;
//        this.isSelected = false;
//    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {

        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public boolean isSelected() {

        return isSelected;
    }

    public void setSelected(boolean selected) {

        isSelected = selected;
    }


}
