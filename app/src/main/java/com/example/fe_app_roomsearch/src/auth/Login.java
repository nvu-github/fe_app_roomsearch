package com.example.fe_app_roomsearch.src.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.fe_app_roomsearch.src.model.user.MUserRes;
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
                login(new MLoginReq(txtUsername.getText().toString(),  txtPassword.getText().toString()));

                Intent intent = new Intent(Login.this, LayoutAdmin.class);
                startActivity(intent);
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
                SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();

                ResponseAPI<MLoginRes> responseFromAPI = response.body();
                String accessToken = responseFromAPI.getData().getAccessToken();
                String refreshToken = responseFromAPI.getData().getRefreshToken();
                Long accessTokenExpires = responseFromAPI.getData().getAccessTokenExpires();
                Long refreshTokenExpires = responseFromAPI.getData().getRefreshTokenExpires();

                editor.putString("accessToken", accessToken);
                editor.putString("refreshToken", refreshToken);
                editor.putLong("accessTokenExpires", System.currentTimeMillis() +  accessTokenExpires);
                editor.putLong("refreshTokenExpires", System.currentTimeMillis() +  refreshTokenExpires);
                editor.apply();
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
