package com.example.fe_app_roomsearch.src.service.user;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISearchService {
    @GET("room/list")
    Call<ResponseAPI<ArrayList<MRoom>>> searchRoom (
            @Query("province") Integer provinceId,
            @Query("district") Integer districtId,
            @Query("ward") Integer wardId,
            @Query("type") String typeRoom
    );
}
