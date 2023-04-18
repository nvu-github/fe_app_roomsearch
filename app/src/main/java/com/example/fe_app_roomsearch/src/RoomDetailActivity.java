package com.example.fe_app_roomsearch.src;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
import com.example.fe_app_roomsearch.src.model.user.room.MRoomRes;
import com.example.fe_app_roomsearch.src.service.IRoomService;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailActivity extends AppCompatActivity {
    private IRoomService roomService;

    private TextView tvName, tvPrice, tvAddress, tvDescription;

    private ImageView imvAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        new FetchData().execute();
        getRoomDetail();

        tvName = findViewById(R.id.room_detail_name);
        tvPrice = findViewById(R.id.room_detail_price);
        tvAddress = findViewById(R.id.room_detail_address);
        tvDescription = findViewById(R.id.room_detail_description);
        imvAvatar = findViewById(R.id.room_detail_avatar);

    }

    private void getRoomDetail(){
        Bundle extras = getIntent().getExtras();
        String roomID = "";
        if (extras != null) {
          roomID = extras.getString(String.valueOf(R.string.roomID));

        }

        roomService = RetrofitClient.getClient(RoomDetailActivity.this).create(IRoomService.class);

        Call<ResponseAPI<MRoom>> call = roomService.getRoomDetail(roomID);
        Log.d(TAG, "getRoomDetail: "+call);
        call.enqueue(new Callback<ResponseAPI<MRoom>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MRoom>> call, Response<ResponseAPI<MRoom>> response) {
                MRoom room = new MRoom();
                room = response.body().getData();
                Locale locale = new Locale("vi", "VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

                tvName.setText(room.getName());
                tvPrice.setText(numberFormat.format(room.getPrice()));
                tvDescription.setText(room.getDescription());
                tvAddress.setText(room.getWard().get_name() +" / "+ room.getDistrict().get_name() +" / "+ room.getProvince().get_name());
                if(room.getAvatar() == null) {
                    imvAvatar.setImageResource(R.drawable.default_image);
                }else{
                    Glide.with(RoomDetailActivity.this).load(getResources().getString(R.string.urlMedia) + room.getAvatar().getUrl()).into(imvAvatar);
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MRoom>> call, Throwable t) {
                Log.d(TAG, "onFailure: query"+t.getMessage());
            }
        });

    }

    private class FetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getRoomDetail();
            return null;
        }
    }

}