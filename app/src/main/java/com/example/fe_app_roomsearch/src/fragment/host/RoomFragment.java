package com.example.fe_app_roomsearch.src.fragment.host;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.ImageUploadAdapter;

import java.util.ArrayList;
import java.util.List;

import com.example.fe_app_roomsearch.src.config.RetrofitClient;
//import com.example.fe_app_roomsearch.src.model.auth.MLogin;
import com.example.fe_app_roomsearch.src.config.RetrofitInterceptor;
import com.example.fe_app_roomsearch.src.model.auth.MLogin;
import com.example.fe_app_roomsearch.src.model.user.UserLogin;
import com.example.fe_app_roomsearch.src.service.IAuth;
import com.example.fe_app_roomsearch.src.service.IUserLogin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RoomFragment extends Fragment {

    Button chooseFileButton;
    private int REQUEST_CODE = 1;
    List<String> images;
    RecyclerView recyclerView;
    Button btnSave;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Spinner spnProvince;
    Spinner spnDistrict;
    Spinner spcWards;
    Spinner spnTypeRoom;
    ArrayAdapter<String> adtProvince;
    ArrayAdapter<String> adtDistrict;
    ArrayAdapter<String> adtWards;
    ArrayAdapter<String> adtTypeRoom;

    IAuth authService;
    MLogin mLogin;
    Retrofit retrofit;

    // sample data
    String[] province = { "HN", "HCM", "Phu Tho"};
    String[] district = { "Nam Tu Liem", "Thanh Tri", "Cau Giay", "Hoang Mai"};
    String[] wards = { "Me Tri", "Dinh Cong", "Khuong Dinh", "Khuong trung"};
    String[] typeRoom = { "Chung cu mini", "Phong tro", "Nha tro", "Nha o"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        authService = RetrofitClient.getClient(getResources().getString(R.string.uriApi)).create(IAuth.class);

        // clear storage
        sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        View view = inflater.inflate(R.layout.layout_admin_host_fragment_room, container, false);

        // init element
        recyclerView = view.findViewById(R.id.rcvImageUpload);
        chooseFileButton = (Button) view.findViewById(R.id.btnChooseFile);
        btnSave = view.findViewById(R.id.btnSave);
        spnProvince = (Spinner) view.findViewById(R.id.province);
        spnDistrict = (Spinner) view.findViewById(R.id.district);
        spcWards = (Spinner) view.findViewById(R.id.wards);
        spnTypeRoom = (Spinner) view.findViewById(R.id.typeRoom);

        // add item to spinner
        adtProvince = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, province);
        adtDistrict = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, district);
        adtWards = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, wards);
        adtTypeRoom = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeRoom);
        spnProvince.setAdapter(adtProvince);
        spnDistrict.setAdapter(adtDistrict);
        spcWards.setAdapter(adtWards);
        spnTypeRoom.setAdapter(adtTypeRoom);

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "click success", Toast.LENGTH_SHORT).show();
                ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {
                    postData("admin","12345");
                    // Gọi API ở đây
                } else {
                    Log.d(TAG, "error network ");
                    // Hiển thị thông báo mất kết nối mạng
                }
//                Call<MLogin> call = authService.getTest();
//
//                call.enqueue(new Callback<MLogin>() {
//                    @Override
//                    public void onResponse(Call<MLogin> call, Response<MLogin> response) {
//                        Log.d(TAG, "onResponse: " + response);
//                    }
//
//                    @Override
//                    public void onFailure(Call<MLogin> call, Throwable t) {
//                        Log.d(TAG, "onResponse error: " + t);
//                    }
//                });
            }
        });

        return view;
    }

    private void postData(String username, String password) {

        // below line is for displaying our progress bar.
//        loadingPB.setVisibility(View.VISIBLE);

        // on below line we are creating a retrofit
        // builder and passing our base url
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RetrofitInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.0.171:3000/api/v1/")
                // as we are sending data in json format so
                // we have to add Gson converter factory
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                // at last we are building our retrofit builder.
                .build();
        // below line is to create an instance for our retrofit api class.
        IUserLogin retrofitAPI = retrofit.create(IUserLogin.class);

        // passing data from our text fields to our modal class.
        UserLogin modal = new UserLogin(username, password);

        // calling a method to create a post and passing our modal class.
        Call<UserLogin> call = retrofitAPI.getData();

        // on below line we are executing our method.
        call.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                // this method is called when we get response from our api.
                Toast.makeText(getContext(), "Data added to API", Toast.LENGTH_SHORT).show();

                // below line is for hiding our progress bar.
//                loadingPB.setVisibility(View.GONE);

                // on below line we are setting empty text
                // to our both edit text.

                // we are getting response from our body
                // and passing it to our modal class.
                UserLogin responseFromAPI = response.body();

                // on below line we are getting our data from modal class and adding it to our string.
                String responseString = "Response Code : " + response.code() + "\nName : " + responseFromAPI.getUsername() + "\n" + "Job : " + responseFromAPI.getPassword();

                // below line we are setting our
                // string to our text view.
                Log.d(TAG, "onResponse: " + responseString);
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Thực hiện các tác vụ mạng với Retrofit ở đây
            return null;
        }
    }

    @Override()
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            images = new ArrayList<>();

            Gson gson = new Gson();

            String getListUri = sharedPreferences.getString("listUri", "");
            Type type = new TypeToken<List<String>>() {}.getType();
            List<String> listUri = gson.fromJson(getListUri, type);
            if (listUri != null) {
                images = listUri;
            }

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    images.add(uri.toString());
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                ImageUploadAdapter adapter = new ImageUploadAdapter(getContext(), images);
                recyclerView.setAdapter(adapter);
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                images.add(uri.toString());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                ImageUploadAdapter adapter = new ImageUploadAdapter(getContext(), images);
                recyclerView.setAdapter(adapter);
            }
            String setListUri = gson.toJson(images);
            editor = sharedPreferences.edit();
            editor.putString("listUri", setListUri);
            editor.apply();
        }
    }
}
