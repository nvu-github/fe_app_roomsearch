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
    @GET("location/province")
    Call<ResponseAPI<MProvinceRes[]>> getProvinces();

    @GET("location/district")
    Call<ResponseAPI<MDistrictRes[]>> getDistricts(@Query("province") String province);

    @GET("location/ward")
    Call<ResponseAPI<MWardRes[]>> getWards(@Query("province") String province, @Query("district") String district);
}
