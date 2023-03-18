package com.example.fe_app_roomsearch.src.adapter;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.auth.Login;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.auth.MLoginRes;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteReq;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.service.IFavoriteService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemHomeRoomNew> mItemHomRoomNew;
    private Context mContext;

    public void setData(Context context, List<ItemHomeRoomNew> mItemHomRoomNew){
        this.mItemHomRoomNew = mItemHomRoomNew;
        this.mContext = context;
//        load data to adapter
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_room_new,parent,false);
        return new ItemCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemHomeRoomNew itemHome = mItemHomRoomNew.get(position);
        if (itemHome == null){
            return;
        }

        ItemCategoryViewHolder itemCategoryViewHolder = (ItemCategoryViewHolder) holder;
        Glide.with(this.mContext).load(itemHome.getAvatar()).into(itemCategoryViewHolder.imageRoom);
        itemCategoryViewHolder.title.setText(itemHome.getTitle());
        itemCategoryViewHolder.price.setText(itemHome.getPrice());
        itemCategoryViewHolder.address.setText(itemHome.getAddress());
        itemCategoryViewHolder.time.setText(itemHome.getTime());
        itemCategoryViewHolder.imvFavourite.setImageResource(itemHome.getFavourite());
        itemCategoryViewHolder.title.setTag(R.string.room, itemHome.getKey());
    }

    @Override
    public int getItemCount() {
        if (mItemHomRoomNew != null){
            return mItemHomRoomNew.size();
        }
        return 0;
    }

    public class ItemCategoryViewHolder extends RecyclerView.ViewHolder{

        private CardView mCard;
        private ImageView imageRoom, imvFavourite;
        private TextView title;
        private TextView price;
        private TextView address;
        private TextView time;

        public ItemCategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.cardId);
            imageRoom = itemView.findViewById(R.id.imageRoom);
            imvFavourite = itemView.findViewById(R.id.favourite);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.address);
            time = itemView.findViewById(R.id.time);

            imvFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String roomId = title.getTag(R.string.room).toString();
                    Log.d(TAG, "onClick: "+roomId);
                    addFavorite(new MFavoriteReq(roomId));
                }
            });
        }

        private void addFavorite(MFavoriteReq mFavoriteReq){
            IFavoriteService favoriteService = RetrofitClient.getClient(mContext.getResources().getString(R.string.uriApi)).create(IFavoriteService.class);
            SharedPreferences prefs = mContext.getSharedPreferences("myPrefs", MODE_PRIVATE);
            String accessToken = prefs.getString("accessToken" , "accessToken");
            Log.d(TAG, "addFavorite: "+mFavoriteReq.getRoom());
            Log.d(TAG, "addFavorite: "+mContext.getResources().getString(R.string.token_type)+accessToken);
            Call<ResponseAPI<MFavoriteRes>> call = favoriteService.addFavorite(mFavoriteReq,mContext.getResources().getString(R.string.token_type)+" "+accessToken);
            call.enqueue(new Callback<ResponseAPI<MFavoriteRes>>() {
                @Override
                public void onResponse(Call<ResponseAPI<MFavoriteRes>> call, Response<ResponseAPI<MFavoriteRes>> response) {
                }

                @Override
                public void onFailure(Call<ResponseAPI<MFavoriteRes>> call, Throwable t) {

                }
            });
        }
    }
}
