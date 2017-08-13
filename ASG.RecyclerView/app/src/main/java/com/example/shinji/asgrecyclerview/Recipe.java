package com.example.shinji.asgrecyclerview;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shinji on 2017/07/24.
 */

public class Recipe implements Parcelable, Cloneable {
    private String name = "";
    private String description = "";
    private int imgUrl = 0;
    private String siteUrl = "";
    private boolean checkSelecte;
    Recipe(String name, String description, int imgUrl, String siteUrl){
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.siteUrl = siteUrl;
        this.checkSelecte = false;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        description = in.readString();
        siteUrl = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName(){
        return this.name;
    }
    public String getDesc(){
        return this.description;
    }
    public int getImgUrl(){ return this.imgUrl;}
    public String getSiteUrl(){ return this.siteUrl;}
    public void setSelected(boolean selected){
        this.checkSelecte = selected;
    }
    public boolean isSelected(){
        return this.checkSelecte;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(siteUrl);
    }
    @Override
    public Recipe clone() throws CloneNotSupportedException {
        Recipe obj = (Recipe) super.clone();
        obj.name = name;
        obj.description = description;
        obj.checkSelecte = false;
        return obj;
    }
}
