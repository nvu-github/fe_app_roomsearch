package com.example.fe_app_roomsearch.src.adapter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.fe_app_roomsearch.src.enums.ScreenName;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.layouts.RoomUserDetailActivity;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteReq;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.service.IFavoriteService;
import com.example.fe_app_roomsearch.src.utils.CurrenciesVND;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemHomeRoomNew> mItemHomRoomNew;
    private Context mContext;
private ScreenName[] isScreen;

    public void setData(Context context, List<ItemHomeRoomNew> mItemHomRoomNew, ScreenName... isScreen){
        this.mItemHomRoomNew = mItemHomRoomNew;
        this.mContext = context;
        this.isScreen = isScreen;
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
        if(itemHome.getAvatar() == "nothing"){
            itemCategoryViewHolder.imageRoom.setImageResource(R.drawable.img_default);
        }else{
            Glide.with(this.mContext).load(itemHome.getAvatar()).into(itemCategoryViewHolder.imageRoom);
        }

        itemCategoryViewHolder.mCard.setTag(R.string.room, itemHome.getKey());
        itemCategoryViewHolder.title.setText(itemHome.getTitle());
        itemCategoryViewHolder.price.setText(itemHome.getPrice());
        itemCategoryViewHolder.address.setText(itemHome.getAddress());
        itemCategoryViewHolder.time.setText(itemHome.getTime());
        itemCategoryViewHolder.showStatus.setText(CurrenciesVND.roomStatus(itemHome.getStatus()));
        itemCategoryViewHolder.imvFavourite.setImageResource(itemHome.getFavourite());
        itemCategoryViewHolder.title.setTag(R.string.room, itemHome.getKey());

        itemCategoryViewHolder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RoomUserDetailActivity.class);
                intent.putExtra(String.valueOf(R.string.roomID), itemCategoryViewHolder.mCard.getTag(R.string.room).toString());
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

    public class ItemCategoryViewHolder extends RecyclerView.ViewHolder{
        private CardView mCard;
        private ImageView imageRoom, imvFavourite;
        private TextView title, price, address, time, showStatus;

        public ItemCategoryViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.cardId);
            imageRoom = itemView.findViewById(R.id.imageRoom);
            imvFavourite = itemView.findViewById(R.id.favourite);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            address = itemView.findViewById(R.id.address);
            time = itemView.findViewById(R.id.time);
            showStatus = itemView.findViewById(R.id.showStatus);

            imvFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String roomId = title.getTag(R.string.room).toString();
                    /*remove room from favorite screen*/
                    if(isScreen.length > 0 && isScreen[0].equals(ScreenName.FAVORITE)){
                        removeFavoriteScreen(roomId);
                    }else{
                        addFavorite(new MFavoriteReq(Integer.parseInt(roomId)));
                    }
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
                    Log.d(TAG, "onResponse: " + response);
                    boolean statusChangeFavorite = responseFromAPI.getData().getStatus();
                    if(statusChangeFavorite){
                        imvFavourite.setImageResource(R.drawable.ic_card_favourite);
                    }else{
                        imvFavourite.setImageResource(R.drawable.ic_card_favourite_none);
                    }
                }

                @Override
                public void onFailure(Call<ResponseAPI<MFavoriteRes>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(mContext, "add favorite fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void removeFavoriteScreen(String roomId){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(true);
            builder.setTitle("Danh sách yêu thích");
            builder.setMessage("Bạn có muốn xóa phòng khỏi danh sách yêu thích");
            builder.setPositiveButton("Xác nhận",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = getPosition();
                            mItemHomRoomNew.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mItemHomRoomNew.size());
                            addFavorite(new MFavoriteReq(Integer.parseInt(roomId)));
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
