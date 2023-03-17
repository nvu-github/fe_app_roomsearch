package com.example.fe_app_roomsearch.src.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
                    imvFavourite.setTag(R.string.favourite, "hidden");
                    Boolean isFavourited = (Boolean) imvFavourite.getTag(R.string.favourite);
                    if(isFavourited){
                        imvFavourite.setTag(R.string.favourite, false);
                    }else{
                        imvFavourite.setTag(R.string.favourite, true);
                    }
                    Log.d(TAG, "fav"+isFavourited.toString());
                }
            });
        }
    }
}
