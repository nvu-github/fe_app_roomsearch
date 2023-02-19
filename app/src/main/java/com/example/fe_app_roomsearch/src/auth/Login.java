package com.example.fe_app_roomsearch.src.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.layouts.LayoutAdmin;

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
                Toast.makeText(Login.this, "login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, LayoutAdmin.class);
                startActivity(intent);
            }
        });
    }
}
