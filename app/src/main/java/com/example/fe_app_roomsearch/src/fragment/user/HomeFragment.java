package com.example.fe_app_roomsearch.src.fragment.user;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import com.example.fe_app_roomsearch.src.helper.TimeHelper;
import com.example.fe_app_roomsearch.src.item.ItemBanner;
import com.example.fe_app_roomsearch.src.item.ItemCategory;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.model.room.MRoom;
import com.example.fe_app_roomsearch.src.model.room.MRoomRes;
import com.example.fe_app_roomsearch.src.service.IFavoriteService;
import com.example.fe_app_roomsearch.src.service.IRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ViewPager mViewBanner;

    private BannerAdapter bannerAdapter;
    private List<ItemBanner> mListItemBanner;
    private CircleIndicator circleIndicator;
    private Timer timer;
    private CategoryAdapter categoryAdapter;
    private RecyclerView listCategory;

    private ImageView imvFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fetch call data api
        new FetchRoom();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_user_fragment_home, container, false);

        // Banner
        mViewBanner = view.findViewById(R.id.view_banner);
        circleIndicator = view.findViewById(R.id.circle_banner);
        mListItemBanner = getListItemBanner();
        bannerAdapter = new BannerAdapter(getActivity(),mListItemBanner);
        mViewBanner.setAdapter(bannerAdapter);
        circleIndicator.setViewPager(mViewBanner);
        bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImage();

        // category
        listCategory = view.findViewById(R.id.listCategory);
        categoryAdapter = new CategoryAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        listCategory.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        listCategory.addItemDecoration(itemDecoration);

        // get rooms list
        getRooms();

        // favorite

        return view;
    }

    private List<ItemBanner> getListItemBanner(){
        List<ItemBanner> itemBanners = new ArrayList<>();
        itemBanners.add(new ItemBanner(R.drawable.banner_search_room_1,1));
        itemBanners.add(new ItemBanner(R.drawable.banner_search_room_2,2));
        return  itemBanners;
    }


    private void autoSlideImage() {
        if (mListItemBanner == null || mListItemBanner.isEmpty() || mViewBanner == null) {
            return;
        }

        // init timer
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


    private class FetchRoom extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getRooms();
            return null;
        }
    }

    private void getRooms(){
        IRoomService roomService = RetrofitClient.getClient(getContext()).create(IRoomService.class);

        Call<ResponseAPI<MRoomRes>> call = roomService.getRooms(1, 10);
        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseAPI<MRoomRes>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MRoomRes>> call, Response<ResponseAPI<MRoomRes>> response) {

                ResponseAPI<MRoomRes> responseFromAPI = response.body();
                ArrayList<MRoom> rooms = responseFromAPI.getData().getItems();

                List<ItemCategory> roomList = new ArrayList<>();
                List<ItemHomeRoomNew> roomNews = new ArrayList<>();

                for (int i = 0; i < rooms.size(); i++) {
                    MRoom room = rooms.get(i);
                    String avatar = "https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg";

                    if(room.getAvatar() != null){
                        avatar = getResources().getString(R.string.urlMedia) + room.getAvatar().getUrl();
                    }
                    if(room.getFavorite() == null){
                        roomNews.add(new ItemHomeRoomNew(String.valueOf(rooms.get(i).getId()),avatar, rooms.get(i).getName(),rooms.get(i).getPrice().toString()+"đ/tháng",rooms.get(i).getMicro_address(), rooms.get(i).getCreated_at(), R.drawable.ic_card_favourite_none));
                    }else{
                        roomNews.add(new ItemHomeRoomNew(String.valueOf(rooms.get(i).getId()),avatar, rooms.get(i).getName(),rooms.get(i).getPrice().toString()+"đ/tháng",rooms.get(i).getMicro_address(), rooms.get(i).getCreated_at(), R.drawable.ic_card_favourite));
                    }
                }

                roomList.add(new ItemCategory("Phòng phòng trọ mới", roomNews));

                categoryAdapter.setData(roomList);
                listCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(Call<ResponseAPI<MRoomRes>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }


        });
    }
}
