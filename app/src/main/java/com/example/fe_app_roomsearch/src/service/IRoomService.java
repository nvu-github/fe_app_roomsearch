package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.room.MRoom;
import com.example.fe_app_roomsearch.src.model.room.MRoomRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IRoomService {

//    @Multipart
//    @POST("upload")
//    Call<ResponseAPI<>> uploadFile(@Part MultipartBody.Part file);

    @GET("room/list")
    Call<ResponseAPI<MRoomRes>> getRooms (@Query("page") Integer page, @Query("limit") Integer limit);

    @GET("room/host/list")
    Call<ResponseAPI<MRoomRes>> getRoomsHost (@Header("Authorization") String authHeader, @Query("page") Integer page, @Query("limit") Integer limit);
}
