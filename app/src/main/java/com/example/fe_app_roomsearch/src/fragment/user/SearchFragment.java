package com.example.fe_app_roomsearch.src.fragment.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.ImageUploadAdapter;
import com.example.fe_app_roomsearch.src.adapter.user.SearchAdapter;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchAdapter searchAdapter;
    private List<ItemHomeRoomNew> mListItemHomeRoomNews;
    private RecyclerView listItemRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_fragment_search, container, false);
        listItemRoom = (RecyclerView) view.findViewById(R.id.listItemRoom);
        mListItemHomeRoomNews = itemHomeRoomNews();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listItemRoom.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(getContext(), mListItemHomeRoomNews);
        listItemRoom.setAdapter(searchAdapter);
        return view;
    }

    private List<ItemHomeRoomNew> itemHomeRoomNews() {
        List<ItemHomeRoomNew> roomNews = new ArrayList<>();
        roomNews.add(new ItemHomeRoomNew("1","https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg", "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        roomNews.add(new ItemHomeRoomNew("2","https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg", "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        return roomNews;
    }
}
