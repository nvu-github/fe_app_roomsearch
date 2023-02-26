package com.example.fe_app_roomsearch.src.model.auth;

import com.google.gson.annotations.SerializedName;

public class MLogin {
    private String user;

    public MLogin(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
