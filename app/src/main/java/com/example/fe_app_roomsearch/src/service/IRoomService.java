package com.example.fe_app_roomsearch.src.service;

import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomReq;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
import com.example.fe_app_roomsearch.src.model.user.room.MRoomRes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IRoomService {

    @Multipart
    @POST("media/upload/multiple")
    Call<ResponseBody> uploadFiles(
        @Part List<MultipartBody.Part> file
    );

    @POST("room")
    Call<ResponseBody> addRoom(@Body MRoomReq body);

    @GET("room/list")
    Call<ResponseAPI<ArrayList<MRoom>>> getRooms();

    @GET("room/list")
    Call<ResponseAPI<ArrayList<MRoom>>> getRoomByProvince(@Query("province") int provinceId);

    @GET("room/host/list")
    Call<ResponseAPI<MRoomRes>> getRoomsHost (@Header("Authorization") String authHeader, @Query("page") Integer page, @Query("limit") Integer limit);
}
