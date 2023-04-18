package com.example.fe_app_roomsearch.src.fragment.host;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.ImageUploadAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.fe_app_roomsearch.src.adapter.host.RoomManagementAdapter;
import com.example.fe_app_roomsearch.src.adapter.user.SearchAdapter;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.fragment.user.SearchFragment;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.item.host.ItemRoom;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomCreateRes;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomReq;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
import com.example.fe_app_roomsearch.src.model.user.room.MRoomRes;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomUploadReq;
import com.example.fe_app_roomsearch.src.model.location.LocationSpinner;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.model.user.search.MSeachReq;
import com.example.fe_app_roomsearch.src.service.ILocationService;
import com.example.fe_app_roomsearch.src.service.IRoomService;
import com.example.fe_app_roomsearch.src.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoomFragment extends Fragment {
    private IRoomService roomService;
    private Button addRoom;
    private RecyclerView listItemRoomHost;
    private List<ItemRoom> itemRoomList;
    private RoomManagementAdapter roomManagementAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_admin_host_fragment_room, container, false);
        roomService = RetrofitClient.getClient(getContext()).create(IRoomService.class);
        addRoom = (Button) view.findViewById(R.id.addRoom);
        listItemRoomHost = (RecyclerView) view.findViewById(R.id.listItemRoomHost);
        new RoomFragment.FetchData().execute();
        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddRoomFragment addRoomFragment = new AddRoomFragment();
                fragmentTransaction.replace(R.id.content_frame, addRoomFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getRoomHost();
            return null;
        }
    }

    private List<ItemRoom> itemRoomHost(ArrayList<MRoom> rooms) {
        List<ItemRoom> roomhost = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            MRoom room = rooms.get(i);
            String avatar = "https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg";
            if(room.getAvatar() != null){
                avatar = getResources().getString(R.string.urlMedia) + room.getAvatar().getUrl();
            }
            roomhost.add(new ItemRoom(
                    String.valueOf(rooms.get(i).getId()),avatar,
                    rooms.get(i).getName(),
                    rooms.get(i).getPrice().toString()+"đ/tháng",
                    rooms.get(i).getCreated_at()
            ));
        }
        return roomhost;
    }

    private void getRoomHost(){
        Call<ResponseAPI<ArrayList<MRoom>>> call = roomService.getRoomsHost();
        call.enqueue(new Callback<ResponseAPI<ArrayList<MRoom>>>() {
            @Override
            public void onResponse(Call<ResponseAPI<ArrayList<MRoom>>> call, Response<ResponseAPI<ArrayList<MRoom>>> response) {
                if (response.isSuccessful()) {
                    ResponseAPI<ArrayList<MRoom>> responseFromAPI = response.body();
                    ArrayList<MRoom> rooms = responseFromAPI.getData();
                    itemRoomList = itemRoomHost(rooms);
                    roomManagementAdapter = new RoomManagementAdapter(getContext(), itemRoomList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    listItemRoomHost.setLayoutManager(layoutManager);
                    listItemRoomHost.setAdapter(roomManagementAdapter);
                }

            }

            @Override
            public void onFailure(Call<ResponseAPI<ArrayList<MRoom>>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
