package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.auth.MLoginReq;
import com.example.fe_app_roomsearch.src.model.auth.MLoginRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {
    @GET("auth/test")
    Call<MLoginRes> getTest();

    @POST("auth/login")
    Call<ResponseAPI<MLoginRes>> login(@Body MLoginReq userLogin);
}
