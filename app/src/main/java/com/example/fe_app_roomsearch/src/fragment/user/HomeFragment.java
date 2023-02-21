package com.example.fe_app_roomsearch.src.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.BannerAdapter;
import com.example.fe_app_roomsearch.src.adapter.CategoryAdapter;
import com.example.fe_app_roomsearch.src.item.ItemBanner;
import com.example.fe_app_roomsearch.src.item.ItemCategory;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    private ViewPager mViewBanner;

    private BannerAdapter bannerAdapter;
    private List<ItemBanner> mListItemBanner;
    private CircleIndicator circleIndicator;
    private Timer timer;
    private CategoryAdapter categoryAdapter;
    private RecyclerView listCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        categoryAdapter.setData(getListCategory());
        listCategory.setAdapter(categoryAdapter);

        return view;
    }

    private List<ItemBanner> getListItemBanner(){
        List<ItemBanner> itemBanners = new ArrayList<>();
        itemBanners.add(new ItemBanner(R.drawable.banner_search_room_1,1));
        itemBanners.add(new ItemBanner(R.drawable.banner_search_room_2,2));
        return  itemBanners;
    }

    private List<ItemCategory> getListCategory() {
        List<ItemCategory> list = new ArrayList<>();

        List<ItemHomeRoomNew> roomNews = new ArrayList<>();
        roomNews.add(new ItemHomeRoomNew("1",R.drawable.banner_search_room_1, "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        roomNews.add(new ItemHomeRoomNew("2",R.drawable.banner_search_room_1, "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        list.add(new ItemCategory("Phòng trọ mới nhất", roomNews));

        List<ItemHomeRoomNew> roomFit = new ArrayList<>();
        roomFit.add(new ItemHomeRoomNew("1",R.drawable.banner_search_room_1, "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        roomFit.add(new ItemHomeRoomNew("2",R.drawable.banner_search_room_1, "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        list.add(new ItemCategory("Phòng trọ phù hợp", roomFit));

        return list;
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
}
