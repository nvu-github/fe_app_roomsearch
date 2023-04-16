package com.example.fe_app_roomsearch.src.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.enums.ScreenName;
import com.example.fe_app_roomsearch.src.item.ItemCategory;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context mContext;
    private List<ItemCategory> mListCategory;

    public FavoriteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ItemCategory> itemCategories){
        this.mListCategory = itemCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_category,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        ItemCategory itemCategory = mListCategory.get(position);
        if (itemCategory == null){
            return;
        }
        holder.nameCategory.setText(itemCategory.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        holder.listCategory.setLayoutManager(linearLayoutManager);

        ItemCategoryAdapter itemCategoryAdapter = new ItemCategoryAdapter();
        itemCategoryAdapter.setData(mContext,itemCategory.getItemCategory(), ScreenName.FAVORITE);
        holder.listCategory.setAdapter(itemCategoryAdapter);
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder{

        private TextView nameCategory;
        private RecyclerView listCategory;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            nameCategory = itemView.findViewById(R.id.nameCategory);
            listCategory = itemView.findViewById(R.id.listCategory);
        }
    }
}
