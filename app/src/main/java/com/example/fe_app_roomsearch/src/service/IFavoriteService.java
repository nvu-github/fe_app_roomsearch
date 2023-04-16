package com.example.fe_app_roomsearch.src.service;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteReq;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteResPag;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IFavoriteService {
    @POST("favorite")
    Call<ResponseAPI<MFavoriteRes>> changeFavorite(@Body() MFavoriteReq favoriteReq);

    @GET("favorite/list")
    Call<ResponseAPI<MFavoriteResPag>> favorites(@Header("Authorization") String accessToken, @Query("page") Integer page, @Query("limit") Integer limit);

}
