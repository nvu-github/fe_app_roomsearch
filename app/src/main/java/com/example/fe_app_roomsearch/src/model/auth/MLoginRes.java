package com.example.fe_app_roomsearch.src.model.auth;

import com.example.fe_app_roomsearch.src.model.user.MUserRes;

public class MLoginRes {
    public String accessToken;
    public String refreshToken;
    public Long accessTokenExpires;
    public Long refreshTokenExpires;

    private MUserRes user;

    public MUserRes getUser() {
        return user;
    }

    public void setUser(MUserRes user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getAccessTokenExpires() {
        return accessTokenExpires;
    }

    public void setAccessTokenExpires(long accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public Long getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(long refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }
}
