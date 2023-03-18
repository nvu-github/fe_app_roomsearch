package com.example.fe_app_roomsearch.src.model.favorite;

public class MFavoriteReq {
    private String room;

    public MFavoriteReq(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
