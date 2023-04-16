package com.example.fe_app_roomsearch.src.model.user.room;

import com.example.fe_app_roomsearch.src.model.MPaginate;

import java.util.ArrayList;

public class MRoomRes {
    private ArrayList<MRoom> items;
    private MPaginate meta;

    public ArrayList<MRoom> getItems() {
        return items;
    }

    public void setItems(ArrayList<MRoom> items) {
        this.items = items;
    }

    public MPaginate getMeta() {
        return meta;
    }

    public void setMeta(MPaginate meta) {
        this.meta = meta;
    }
}
