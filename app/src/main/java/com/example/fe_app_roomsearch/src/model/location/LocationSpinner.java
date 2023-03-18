package com.example.fe_app_roomsearch.src.model.location;

public class LocationSpinner {
    public String name;
    public Integer tag;

    public LocationSpinner(String name, Integer tag){
        this.name = name;
        this.tag = tag;

    }

    @Override()
    public String toString(){
        return name;
    }
}
