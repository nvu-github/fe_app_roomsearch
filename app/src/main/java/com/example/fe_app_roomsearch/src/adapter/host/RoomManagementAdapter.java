package com.example.fe_app_roomsearch.src.adapter.host;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.host.ItemRoom;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomDeleteRes;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
import com.example.fe_app_roomsearch.src.service.IRoomService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IRoomService roomService;

    private List<ItemRoom> mItemRoom;
    private Context mContext;

    public RoomManagementAdapter(Context context, List<ItemRoom> ItemRoom){
        this.mContext = context;
        this.mItemRoom = ItemRoom;
        roomService = RetrofitClient.getClient(mContext).create(IRoomService.class);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host_room,parent,false);
        return new RoomManagementAdapter.RoomManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemRoom itemRoom = mItemRoom.get(position);
        if (itemRoom == null){
            return;
        }

        RoomManagementAdapter.RoomManagementViewHolder roomManagementViewHolder = (RoomManagementAdapter.RoomManagementViewHolder) holder;
        if(itemRoom.getAvatar() == "nothing"){
            roomManagementViewHolder.imageRoom.setImageResource(R.drawable.img_default);
        }else{
            Glide.with(this.mContext).load(itemRoom.getAvatar()).into(roomManagementViewHolder.imageRoom);
        }
        roomManagementViewHolder.title.setText(itemRoom.getTitle());
        roomManagementViewHolder.price.setText(itemRoom.getPrice());
        roomManagementViewHolder.time.setText(itemRoom.getTime());
        roomManagementViewHolder.title.setTag(R.string.room, itemRoom.getKey());
        roomManagementViewHolder.btn_edit.setTag(R.string.room, itemRoom.getKey());
        roomManagementViewHolder.btn_delete.setTag(R.string.room, itemRoom.getKey());

        roomManagementViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Delete success" + itemRoom.getKey(), Toast.LENGTH_SHORT).show();

                Call<ResponseAPI<MRoomDeleteRes>> call = roomService.deleteRoom(itemRoom.getKey());
                call.enqueue(new Callback<ResponseAPI<MRoomDeleteRes>>() {
                    @Override
                    public void onResponse(Call<ResponseAPI<MRoomDeleteRes>> call, Response<ResponseAPI<MRoomDeleteRes>> response) {
                        if (response.isSuccessful()) {
                            ResponseAPI<MRoomDeleteRes> responseFromAPI = response.body();
                            Log.d(TAG, "onResponse: " + responseFromAPI.getData());
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseAPI<MRoomDeleteRes>> call, Throwable t) {
                        Log.d(MotionEffect.TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItemRoom != null){
            return mItemRoom.size();
        }
        return 0;
    }

    public class RoomManagementViewHolder extends RecyclerView.ViewHolder{

        private CardView mCard;
        private ImageView imageRoom;
        private TextView title;
        private TextView price;
        private TextView time;
        private Button btn_edit, btn_delete;

        public RoomManagementViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.cardId);
            imageRoom = itemView.findViewById(R.id.imageRoom);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            time = itemView.findViewById(R.id.time);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
