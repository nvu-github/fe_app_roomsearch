package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.auth.MLoginRes;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ILocationService {
    @GET("location/provinces")
    Call<ResponseAPI<MProvinceRes[]>> getProvinces();

    @GET("location/districts")
    Call<ResponseAPI<MDistrictRes[]>> getDistricts();

    @GET("location/wards")
    Call<ResponseAPI<MWardRes[]>> getWards();
}
