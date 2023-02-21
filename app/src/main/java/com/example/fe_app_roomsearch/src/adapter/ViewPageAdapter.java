package com.example.fe_app_roomsearch.src.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fe_app_roomsearch.src.fragment.user.FavouriteFragment;
import com.example.fe_app_roomsearch.src.fragment.user.HomeFragment;
import com.example.fe_app_roomsearch.src.fragment.user.SearchFragment;
import com.example.fe_app_roomsearch.src.fragment.user.UserFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new HomeFragment();
            case 1:
                return  new SearchFragment();
            case 2:
                return  new FavouriteFragment();
            case 3:
                return  new UserFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}

