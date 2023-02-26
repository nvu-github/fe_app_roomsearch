package com.example.fe_app_roomsearch.src.config;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetrofitInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        // thêm các header cần thiết vào request
        // ví dụ:
        // requestBuilder.addHeader("Authorization", "Bearer " + token);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}