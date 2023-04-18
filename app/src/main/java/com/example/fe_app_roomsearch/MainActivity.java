package com.example.fe_app_roomsearch;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.fe_app_roomsearch.src.adapter.ViewPageAdapter;
import com.example.fe_app_roomsearch.src.auth.Login;
import com.example.fe_app_roomsearch.src.layouts.LayoutAdmin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewLayout;
    private BottomNavigationView menuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewLayout = (ViewPager) findViewById(R.id.viewLayout);
        menuLayout = (BottomNavigationView) findViewById(R.id.menuLayout);

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewLayout.setAdapter(adapter);

        viewLayout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        menuLayout.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        menuLayout.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                    case 2:
                        menuLayout.getMenu().findItem(R.id.menu_favourite).setChecked(true);
                        break;
                    case 3:
                        menuLayout.getMenu().findItem(R.id.menu_user).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        menuLayout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        viewLayout.setCurrentItem(0);
                        break;
                    case R.id.menu_search:
                        viewLayout.setCurrentItem(1);
                        break;
                    case R.id.menu_favourite:
                        viewLayout.setCurrentItem(2);
                        break;
                    case R.id.menu_user:
                        viewLayout.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });
    }

    private Boolean isLogged(){
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);

        Long refreshTokenExpires = prefs.getLong("refreshTokenExpires", -1);
        if (refreshTokenExpires > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }
}