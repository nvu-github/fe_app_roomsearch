package com.example.fe_app_roomsearch.src.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.item.ItemBanner;

import com.bumptech.glide.Glide;
import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ItemBanner> mItemBanner;

    public BannerAdapter(Context mContext, List<ItemBanner> mItemBanner) {
        this.mContext = mContext;
        this.mItemBanner = mItemBanner;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_home_banner,container,false);
        ImageView imageView = view.findViewById(R.id.img_banner);

        ItemBanner itemBanner = mItemBanner.get(position);
        if (itemBanner != null){
            Glide.with(mContext).load(itemBanner.getResourceId()).into(imageView);
        }
//        Add view to viewgroup
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (mItemBanner != null){
            return mItemBanner.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
//        remove view
        container.removeView((View) object);
    }
}
