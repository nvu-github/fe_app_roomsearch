package com.example.fe_app_roomsearch.src.model.auth;

public class MLoginRes {
    public String accessToken;
    public String refreshToken;
    public int accessTokenExpires;
    public int refreshTokenExpires;

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

    public int getAccessTokenExpires() {
        return accessTokenExpires;
    }

    public void setAccessTokenExpires(int accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public int getRefreshTokenExpires() {
        return refreshTokenExpires;
    }

    public void setRefreshTokenExpires(int refreshTokenExpires) {
        this.refreshTokenExpires = refreshTokenExpires;
    }
}
