package com.example.fe_app_roomsearch.src.auth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fe_app_roomsearch.MainActivity;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.fragment.user.UserFragment;
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
    private Button btnLogin, btnBack;

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_login);
        txtUsername = (EditText) findViewById(R.id.username);
        txtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(new MLoginReq(txtUsername.getText().toString(),  txtPassword.getText().toString()));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(MLoginReq loginReq){
        AuthService authService = RetrofitClient.getClient(this).create(AuthService.class);
        Call<ResponseAPI<MLoginRes>> call = authService.login(loginReq);
        call.enqueue(new Callback<ResponseAPI<MLoginRes>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MLoginRes>> call, Response<ResponseAPI<MLoginRes>> response) {
                ResponseAPI<MLoginRes> responseFromAPI = response.body();
                if(responseFromAPI == null){
                    Toast.makeText(Login.this, "Sai tài khoản, mật khẩu. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                String accessToken = responseFromAPI.getData().getAccessToken();
                String role = responseFromAPI.getData().getRole();
                Long accessTokenExpires = responseFromAPI.getData().getAccessTokenExpires();
                Long refreshTokenExpires = responseFromAPI.getData().getRefreshTokenExpires();

                editor.putString("accessToken", accessToken);
                editor.putString("isLoggedIn", "true");
                editor.putString("fullName", responseFromAPI.getData().getFullName());
                editor.putString("role", role);
                editor.putLong("accessTokenExpires", System.currentTimeMillis() +  accessTokenExpires);
                editor.putLong("refreshTokenExpires", System.currentTimeMillis() +  refreshTokenExpires);
                editor.apply();

                if (role.equalsIgnoreCase("user")) {
                    intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(Login.this, LayoutAdmin.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MLoginRes>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: login" + t.getMessage());
            }
        });
    }
}
