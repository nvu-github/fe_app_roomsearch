package com.example.fe_app_roomsearch.src.adapter.user;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.layouts.RoomUserDetailActivity;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteReq;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.service.IFavoriteService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemHomeRoomNew> mItemHomRoomNew;
    private Context mContext;

    public SearchAdapter(Context context, List<ItemHomeRoomNew> mItemHomRoomNew){
        this.mItemHomRoomNew = mItemHomRoomNew;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_room_new,parent,false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHomeRoomNew itemHome = mItemHomRoomNew.get(position);
        if (itemHome == null){
            return;
        }

        SearchAdapter.SearchViewHolder searchViewHolder = (SearchAdapter.SearchViewHolder) holder;
        Glide.with(this.mContext).load(itemHome.getAvatar()).into(searchViewHolder.imageRoom);
        searchViewHolder.mCard.setTag(R.string.room, itemHome.getKey());
        searchViewHolder.title.setText(itemHome.getTitle());
        searchViewHolder.price.setText(itemHome.getPrice());
        searchViewHolder.address.setText(itemHome.getAddress());
        searchViewHolder.time.setText(itemHome.getTime());
        searchViewHolder.imvFavourite.setImageResource(itemHome.getFavourite());
        searchViewHolder.title.setTag(R.string.room, itemHome.getKey());

        searchViewHolder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RoomUserDetailActivity.class);
                intent.putExtra(String.valueOf(R.string.roomID), searchViewHolder.mCard.getTag(R.string.room).toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItemHomRoomNew != null){
            return mItemHomRoomNew.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private CardView mCard;
        private ImageView imageRoom, imvFavourite;
        private TextView title;
        private TextView price;
        private TextView address;
        private TextView time;

        public SearchViewHolder(@NonNull @NotNull View itemView) {
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
                    addFavorite(new MFavoriteReq(Integer.parseInt(roomId)));
                }
            });
        }

        private void addFavorite(MFavoriteReq mFavoriteReq){
            IFavoriteService favoriteService = RetrofitClient.getClient(mContext).create(IFavoriteService.class);

            Call<ResponseAPI<MFavoriteRes>> call = favoriteService.changeFavorite(mFavoriteReq);
            call.enqueue(new Callback<ResponseAPI<MFavoriteRes>>() {
                @Override
                public void onResponse(Call<ResponseAPI<MFavoriteRes>> call, Response<ResponseAPI<MFavoriteRes>> response) {
                    ResponseAPI<MFavoriteRes> responseFromAPI = response.body();
                    boolean statusChangeFavorite = responseFromAPI.getData().getStatus();
                    if(statusChangeFavorite){
                        imvFavourite.setImageResource(R.drawable.ic_card_favourite);
                    }else{
                        imvFavourite.setImageResource(R.drawable.ic_card_favourite_none);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAPI<MFavoriteRes>> call, Throwable t) {
                    Toast.makeText(mContext, "add favorite fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
