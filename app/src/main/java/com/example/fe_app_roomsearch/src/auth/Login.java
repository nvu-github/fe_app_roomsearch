package com.example.fe_app_roomsearch.src.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.config.RetrofitInterceptor;
import com.example.fe_app_roomsearch.src.layouts.LayoutAdmin;
import com.example.fe_app_roomsearch.src.model.user.UserLogin;
import com.example.fe_app_roomsearch.src.service.IUserLogin;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                postData("username", "password");
//                Toast.makeText(Login.this, "login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, LayoutAdmin.class);
                startActivity(intent);
            }
        });
    }

    private void postData(String username, String password) {

        // below line is for displaying our progress bar.
//        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new RetrofitInterceptor())
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.0.171:300[0/api/v1/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        IUserLogin retrofitAPI = retrofit.create(IUserLogin.class);

        Call<UserLogin> call = retrofitAPI.getData();

        // on below line we are executing our method.
        call.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {

                UserLogin responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getUsername() + "\n" + "Job : " + responseFromAPI.getPassword();

                // below line we are setting our
                // string to our text view.
                Log.d(TAG, "onResponse: " + responseString);
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
