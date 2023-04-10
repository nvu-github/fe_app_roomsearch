package com.example.fe_app_roomsearch.src.model.host.room;

public class MRoomUploadReq {
    private String user, room, tag;

    public MRoomUploadReq(String user, String room, String tag) {
        this.user = user;
        this.room = room;
        this.tag = tag;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
