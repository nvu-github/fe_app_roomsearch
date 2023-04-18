package com.example.fe_app_roomsearch.src.fragment.user;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.BannerAdapter;
import com.example.fe_app_roomsearch.src.adapter.CategoryAdapter;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.ItemBanner;
import com.example.fe_app_roomsearch.src.item.ItemCategory;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
import com.example.fe_app_roomsearch.src.model.user.room.MRoomRes;
import com.example.fe_app_roomsearch.src.service.IRoomService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private  IRoomService roomService;
    private ViewPager mViewBanner;
    private BannerAdapter bannerAdapter;
    private CircleIndicator circleIndicator;
    private Timer timer;
    private CategoryAdapter categoryAdapter;
    private RecyclerView listCategory;

    private List<ItemCategory> roomList;
    private List<ItemBanner> mListItemBanner;

    private ImageView imvFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_fragment_home, container, false);
        initView(view);
        autoSlideImage();
        setAdapterDefault();
        new FetchData().execute();

        return view;
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getRoomNews();
            getRoomByProvince(1);
            getRoomByProvince(2);
            return null;
        }
    }

    private void initView(View view) {
        roomService = RetrofitClient.getClient(getContext()).create(IRoomService.class);
        mViewBanner = view.findViewById(R.id.view_banner);
        circleIndicator = view.findViewById(R.id.circle_banner);
        listCategory = view.findViewById(R.id.listCategory);

        roomList = new ArrayList<>();
    }

    private void setAdapterDefault() {
        mListItemBanner = getListItemBanner();
        bannerAdapter = new BannerAdapter(getActivity(),mListItemBanner);
        categoryAdapter = new CategoryAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        listCategory.setLayoutManager(linearLayoutManager);
        listCategory.addItemDecoration(itemDecoration);
        mViewBanner.setAdapter(bannerAdapter);
        circleIndicator.setViewPager(mViewBanner);
        bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    private List<ItemBanner> getListItemBanner(){
        List<ItemBanner> itemBanners = new ArrayList<>();
        itemBanners.add(new ItemBanner(R.drawable.banner_search_room_1,1));
        itemBanners.add(new ItemBanner(R.drawable.banner_search_room_2,2));
        return  itemBanners;
    }

    private List<ItemHomeRoomNew> getItemHomeRoom(ArrayList<MRoom> rooms) {
        List<ItemHomeRoomNew> roomNews = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            int favouriteIcon = R.drawable.ic_card_favourite_none;
            MRoom room = rooms.get(i);
            String avatar = "https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg";

            if(room.getAvatar() != null){
                avatar = getResources().getString(R.string.urlMedia) + room.getAvatar().getUrl();
            }

            if(room.getFavorite() != null) {
                favouriteIcon = R.drawable.ic_card_favourite;
            }
            roomNews.add(new ItemHomeRoomNew(
                    String.valueOf(rooms.get(i).getId()),avatar,
                    rooms.get(i).getName(),
                    rooms.get(i).getPrice().toString()+"đ/tháng",
                    rooms.get(i).getMicro_address() + ", " + rooms.get(i).getAddress(),
                    rooms.get(i).getCreated_at(),
                    favouriteIcon
            ));
        }
        return roomNews;
    }

    private void getRoomNews(){
        Call<ResponseAPI<ArrayList<MRoom>>> call = roomService.getRooms();
        call.enqueue(new Callback<ResponseAPI<ArrayList<MRoom>>>() {
            @Override
            public void onResponse(Call<ResponseAPI<ArrayList<MRoom>>> call, Response<ResponseAPI<ArrayList<MRoom>>> response) {
                ResponseAPI<ArrayList<MRoom>> responseFromAPI = response.body();
                ArrayList<MRoom> rooms = responseFromAPI.getData();
                Log.d(TAG, "onResponse: " + rooms);
                List<ItemHomeRoomNew> roomNews = getItemHomeRoom(rooms);
                roomList.add(new ItemCategory("Phòng trọ mới", roomNews));
                categoryAdapter.setData(roomList);
                listCategory.setAdapter(categoryAdapter);

                if (response.isSuccessful()) {

                }
                else {
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("TAG", "Error body: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<ArrayList<MRoom>>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getRoomByProvince(int provinceId) {
        Call<ResponseAPI<ArrayList<MRoom>>> call = roomService.getRoomByProvince(provinceId);
        call.enqueue(new Callback<ResponseAPI<ArrayList<MRoom>>>() {
            @Override
            public void onResponse(Call<ResponseAPI<ArrayList<MRoom>>> call, Response<ResponseAPI<ArrayList<MRoom>>> response) {
                ResponseAPI<ArrayList<MRoom>> responseFromAPI = response.body();
                ArrayList<MRoom> rooms = responseFromAPI.getData();

                List<ItemHomeRoomNew> roomNews = getItemHomeRoom(rooms);
                if (provinceId == 1) {
                    roomList.add(new ItemCategory("Phòng trọ khu vực TP.HCM", roomNews));
                } else {
                    roomList.add(new ItemCategory("Phòng trọ khu vực TP.HN", roomNews));
                }
                categoryAdapter.setData(roomList);
                listCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(Call<ResponseAPI<ArrayList<MRoom>>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void autoSlideImage() {
        if (mListItemBanner == null || mListItemBanner.isEmpty() || mViewBanner == null) {
            return;
        }

        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = mViewBanner.getCurrentItem();
                        int totalItem = mListItemBanner.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            mViewBanner.setCurrentItem(currentItem);
                        } else {
                            mViewBanner.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
}
