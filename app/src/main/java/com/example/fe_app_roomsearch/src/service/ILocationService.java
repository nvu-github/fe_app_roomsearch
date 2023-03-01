package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.auth.MLoginRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ILocationService {
    @GET("location/province")
    Call<ResponseAPI<MProvinceRes[]>> getProvinces();
}
