package com.example.fe_app_roomsearch.src.config;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import com.example.fe_app_roomsearch.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static final String baseUrl = "http://192.168.1.134:8000/api/v1/";

    public static Retrofit getClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RetrofitInterceptor(context))
                .addInterceptor(logging)
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
