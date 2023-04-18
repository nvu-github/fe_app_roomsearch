package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.user.MUserRes;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IUserService {
    @GET("user/profile")
    Call<ResponseAPI<MUserRes>> getProfile();
}
