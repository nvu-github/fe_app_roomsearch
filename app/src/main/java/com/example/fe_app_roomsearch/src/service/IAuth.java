package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.auth.MLogin;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IAuth {
    @GET("auth/test")
    Call<MLogin> getTest();
}
