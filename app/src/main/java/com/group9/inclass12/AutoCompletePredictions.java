package com.group9.inclass12;

public class AutoCompletePredictions {
    String description;
    String id;
    String place_id ;

    public AutoCompletePredictions(String description, String id, String place_id) {
        this.description = description;
        this.id = id;
        this.place_id = place_id;
    }

    public AutoCompletePredictions( ) {

    }

    @Override
    public String toString() {
        return "AutoCompletePredictions{" +
                "description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", place_id='" + place_id + '\'' +
                '}';
    }
}
