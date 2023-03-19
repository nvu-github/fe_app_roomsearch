package com.example.fe_app_roomsearch.src.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.layouts.LayoutAdmin;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.auth.MLoginReq;
import com.example.fe_app_roomsearch.src.model.auth.MLoginRes;
import com.example.fe_app_roomsearch.src.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText txtUsername, txtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login);
        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login( new MLoginReq(txtUsername.getText().toString(),  txtPassword.getText().toString()));

//                Intent intent = new Intent(Login.this, LayoutAdmin.class);
//                startActivity(intent);
            }
        });
    }

    private void login(MLoginReq loginReq){
        AuthService authService = RetrofitClient.getClient(getResources().getString(R.string.uriApi)).create(AuthService.class);
        Call<ResponseAPI<MLoginRes>> call = authService.login(loginReq);
        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseAPI<MLoginRes>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MLoginRes>> call, Response<ResponseAPI<MLoginRes>> response) {

                ResponseAPI<MLoginRes> responseFromAPI = response.body();
                Log.d(TAG, "onResponse: "+response.body());
                Log.d(TAG, "accessToken" + responseFromAPI.getData().getAccessToken());
                Log.d(TAG, "refreshToken: " + responseFromAPI.getData().getRefreshToken());
                Log.d(TAG, "accessTokenEx" + responseFromAPI.getData().getAccessTokenExpires());
                Log.d(TAG, "refreshTokenEx: " + responseFromAPI.getData().getRefreshTokenExpires());

            }

            @Override
            public void onFailure(Call<ResponseAPI<MLoginRes>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
