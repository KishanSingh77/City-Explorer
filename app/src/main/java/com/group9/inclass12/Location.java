package com.group9.inclass12;

//In-Class12
//Group 9
//Rockford Stoller
//Ryan Swaim

public class Location {
    String lat;
    String lng;
    String name;

    public Location() {
    }

    public Location(String lat, String lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }
}
