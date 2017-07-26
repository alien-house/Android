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
    private boolean checkSelecte;
    Recipe(String name, String description, int imgUrl){
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.checkSelecte = false;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        description = in.readString();
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
    public int getImgUrl(){
        return this.imgUrl;
    }
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
