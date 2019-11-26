package com.group9.inclass12;

import java.util.ArrayList;


public class Trip {

    public String tripName;
    public Location destinationCity;
    public ArrayList<Location> locations = new ArrayList<>();
    public String tripDate;

    public Trip() {
    }

    public Trip(String tripName, Location destinationCity, ArrayList<Location> locations, String tripDate) {
        this.tripName = tripName;
        this.destinationCity = destinationCity;
        this.locations = locations;
        this.tripDate = tripDate;
    }
}
