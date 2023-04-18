package com.example.fe_app_roomsearch.src;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fe_app_roomsearch.MainActivity;
import com.example.fe_app_roomsearch.src.fragment.user.UserFragment;
import com.example.fe_app_roomsearch.src.layouts.LayoutAdmin;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                String role = prefs.getString("role" , "");
                Log.d(TAG, "splashScreen: " + role);
                if (role.equalsIgnoreCase("host") || role.equalsIgnoreCase("admin")) {
                    intent = new Intent(SplashScreen.this, LayoutAdmin.class);
                } else {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },1000);

    }
}
