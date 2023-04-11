package com.example.fe_app_roomsearch.src.config;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitInterceptor implements Interceptor {

    private SharedPreferences prefs;
    private String accessToken;

    public RetrofitInterceptor(Context context) {
        prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (prefs.contains("accessToken")) {
            accessToken = prefs.getString("accessToken" , "accessToken");
        }

        Request.Builder requestBuilder = chain.request().newBuilder();
        // thêm các header cần thiết vào request
        // ví dụ:
        requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}