package com.example.fe_app_roomsearch.src.config;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.fe_app_roomsearch.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RetrofitInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d(TAG, "Connect server success!" + baseUrl);
        return retrofit;
    }
}
