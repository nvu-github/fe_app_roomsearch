package com.example.fe_app_roomsearch.src.service;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteReq;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IFavoriteService {
    @POST("favorite")
    Call<ResponseAPI<MFavoriteRes>> changeFavorite(@Body() MFavoriteReq favoriteReq, @Header("Authorization") String accessToken);

    @GET("favorite")
    Call<ResponseAPI<MFavoriteRes>> favorites(@Header("Authorization") String accessToken);
}
