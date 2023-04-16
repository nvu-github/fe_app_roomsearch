package com.example.fe_app_roomsearch.src.model.favorite;

import com.example.fe_app_roomsearch.src.model.room.MRoom;

public class MFavorite {
    private int id;
    private MRoom room;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MRoom getRoom() {
        return room;
    }

    public void setRoom(MRoom room) {
        this.room = room;
    }
}
