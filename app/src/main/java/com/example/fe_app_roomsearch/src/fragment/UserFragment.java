package com.example.fe_app_roomsearch.src.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fe_app_roomsearch.R;

public class UserFragment extends Fragment {

    private TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_user_fragment_user, container, false);
//        view.findViewById()
        return view;
    }
}
