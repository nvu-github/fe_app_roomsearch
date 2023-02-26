package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.user.UserLogin;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IUserLogin {
    @GET("auth/test")
    Call<UserLogin> getData();
}
