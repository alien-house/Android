package com.alienhouse.kitten;

import java.util.Comparator;

/**
 * Created by shinji on 2017/08/31.
 */

public class LocationData implements Comparable<LocationData> {
    String long_name;
    String short_name;
    String[] types = {};
    LocationData(String long_name, String short_name, String[] types){
        this.long_name = long_name;
        this.short_name = short_name;
        this.types = types;
//        this.types = Arrays.copyOf(types, types.length, String[].class);
    }

    @Override
    public int compareTo(LocationData locationData) {
        return (types[0].compareTo(locationData.types[0]));
    }

    public static Comparator<LocationData> getLocationDataComparable(){
        return new Comparator<LocationData>() {
            @Override
            public int compare(LocationData o1, LocationData o2) {
                return o1.types[0].compareTo(o2.types[0]);
            }
        };
    }


}