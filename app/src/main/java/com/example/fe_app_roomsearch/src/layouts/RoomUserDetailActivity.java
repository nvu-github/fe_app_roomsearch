package com.example.fe_app_roomsearch.src.layouts;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.fe_app_roomsearch.MainActivity;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.BannerAdapter;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.ItemBanner;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.user.media.MediaRes;
import com.example.fe_app_roomsearch.src.model.user.room.MRoomDetailRes;
import com.example.fe_app_roomsearch.src.service.IRoomService;
import com.example.fe_app_roomsearch.src.utils.CurrenciesVND;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomUserDetailActivity extends AppCompatActivity {

    private ViewPager mViewMedia;
    private BannerAdapter mediaAdapter;
    private List<ItemBanner> mListItemMedia;
    private CircleIndicator circleIndicator;

    private IRoomService roomService;

    private TextView roomName, roomPrice, roomAddress, roomAcreage, roomStatus, roomType, roomDescription, hostName, hostPhone;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_room_detail);
        initView();
        new RoomUserDetailActivity.FetchData().execute();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomUserDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        hostPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = hostPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
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

    private void initView() {
        mViewMedia = (ViewPager) findViewById(R.id.view_banner);
        roomName = (TextView) findViewById(R.id.roomName);
        roomPrice = (TextView) findViewById(R.id.roomPrice);
        roomAddress = (TextView) findViewById(R.id.roomAddress);
        roomAcreage = (TextView) findViewById(R.id.roomAcreage);
        roomStatus = (TextView) findViewById(R.id.roomStatus);
        roomType = (TextView) findViewById(R.id.roomType);
        roomDescription = (TextView) findViewById(R.id.roomDescription);
        hostName = (TextView) findViewById(R.id.nameHost);
        hostPhone = (TextView) findViewById(R.id.phoneHost);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_banner);
        btnBack = (Button) findViewById(R.id.btnBack);

        hostPhone.setPaintFlags(hostPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private List<ItemBanner> getListItemBanner(List<MediaRes> mediaRes){
        List<ItemBanner> itemBanners = new ArrayList<>();
        for (int i = 0; i < mediaRes.size(); i++) {
            itemBanners.add(new ItemBanner(i, (i + 1), mediaRes.get(i).getUrl(), true));
        }
        return  itemBanners;
    }

    private void getRoomDetail(){
        Bundle extras = getIntent().getExtras();
        String roomID = "";
        if (extras != null) {
            roomID = extras.getString(String.valueOf(R.string.roomID));

        }

        roomService = RetrofitClient.getClient(this).create(IRoomService.class);
        Call<ResponseAPI<MRoomDetailRes>> call = roomService.getRoomDetail(roomID);
        call.enqueue(new Callback<ResponseAPI<MRoomDetailRes>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MRoomDetailRes>> call, Response<ResponseAPI<MRoomDetailRes>> response) {
                MRoomDetailRes room = response.body().getData();
                mListItemMedia = getListItemBanner(room.getMedias());

                mediaAdapter = new BannerAdapter(RoomUserDetailActivity.this, mListItemMedia);
                mViewMedia.setAdapter(mediaAdapter);
                circleIndicator.setViewPager(mViewMedia);
                mediaAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

                roomName.setText(room.getName());
                roomPrice.setText(CurrenciesVND.formatted(room.getPrice().toString())+"/tháng");
                roomAddress.setText(room.getAddress());
                roomAcreage.setText(room.getAcreage().toString());
                roomStatus.setText(CurrenciesVND.roomStatus(room.getStatus()));
                roomType.setText(room.getType());
                roomDescription.setText(room.getDescription());
                hostName.setText(room.getUser().getFullName());
                hostPhone.setText(room.getUser().getPhone());
            }

            @Override
            public void onFailure(Call<ResponseAPI<MRoomDetailRes>> call, Throwable t) {
                Log.d(TAG, "onFailure: query"+t.getMessage());
            }
        });

    }
}
