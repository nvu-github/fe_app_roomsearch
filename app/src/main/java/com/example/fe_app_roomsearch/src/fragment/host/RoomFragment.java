package com.example.fe_app_roomsearch.src.fragment.host;

import static android.app.Activity.RESULT_OK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.example.fe_app_roomsearch.src.model.auth.MLogin;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.config.RetrofitInterceptor;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.auth.MLoginRes;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.service.AuthService;
import com.example.fe_app_roomsearch.src.service.ILocationService;
import com.example.fe_app_roomsearch.src.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
    Spinner spnWards;
    Spinner spnTypeRoom;
    ArrayAdapter<String> adtProvince;
    ArrayAdapter<String> adtDistrict;
    ArrayAdapter<String> adtWards;
    ArrayAdapter<String> adtTypeRoom;

    AuthService authService;
    MLoginRes mLogin;
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
        spnWards = (Spinner) view.findViewById(R.id.wards);
        spnTypeRoom = (Spinner) view.findViewById(R.id.typeRoom);

        // add item to spinner
        new FetchData().execute();
        adtDistrict = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, district);
        adtWards = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, wards);
        adtTypeRoom = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeRoom);
        spnProvince.setAdapter(adtProvince);
        spnDistrict.setAdapter(adtDistrict);
        spnWards.setAdapter(adtWards);
        spnTypeRoom.setAdapter(adtTypeRoom);

        spinnerPick();
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

    private class FetchData extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            getProvinces();
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

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
    }

//    private void uploadFile(String filePath) {
//        File file = new File(filePath);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .build();
//
//        MyAPI api = retrofit.create(MyAPI.class);
//        Call<ResponseBody> call = api.uploadFile(body);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                // Xử lý kết quả trả về nếu cần thiết
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // Xử lý lỗi nếu có
//            }
//        });
//    }


    private void getProvinces(){
        ILocationService locationService = RetrofitClient.getClient(getResources().getString(R.string.uriApi)).create(ILocationService.class);
        Call<ResponseAPI<MProvinceRes[]>> call = locationService.getProvinces();
        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseAPI<MProvinceRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MProvinceRes[]>> call, Response<ResponseAPI<MProvinceRes[]>> response) {

                ResponseAPI<MProvinceRes[]> responseFromAPI = response.body();
                List<String>  provinces  = new ArrayList<>();
                for (int i = 0; i < responseFromAPI.getData().length; i++) {
                    provinces.add(responseFromAPI.getData()[i].get_name()) ;
                }
                adtProvince =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, provinces);
                spnProvince.setAdapter(adtProvince);
            }

            @Override
            public void onFailure(Call<ResponseAPI<MProvinceRes[]>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }


        });
    }

    private void spinnerPick(){
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getDistrict(String.valueOf(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Integer currentProvince= spnProvince.getSelectedItemPosition();
                getWards("35", "442");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void getDistrict(String provinceId){
        ILocationService locationService = RetrofitClient.getClient(getResources().getString(R.string.uriApi)).create(ILocationService.class);
        Call<ResponseAPI<MDistrictRes[]>> call = locationService.getDistricts(provinceId);
        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseAPI<MDistrictRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MDistrictRes[]>> call, Response<ResponseAPI<MDistrictRes[]>> response) {

                ResponseAPI<MDistrictRes[]> responseFromAPI = response.body();
                List<String>  districts  = new ArrayList<>();
                for (int i = 0; i < responseFromAPI.getData().length; i++) {
                    districts.add(responseFromAPI.getData()[i].get_name()) ;
                }
                adtDistrict =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, districts);
                spnDistrict.setAdapter(adtDistrict);
            }

            @Override
            public void onFailure(Call<ResponseAPI<MDistrictRes[]>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getWards(String provinceId, String districtId){
        ILocationService locationService = RetrofitClient.getClient(getResources().getString(R.string.uriApi)).create(ILocationService.class);
        Call<ResponseAPI<MWardRes[]>> call = locationService.getWards(provinceId, districtId);
        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseAPI<MWardRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MWardRes[]>> call, Response<ResponseAPI<MWardRes[]>> response) {

                ResponseAPI<MWardRes[]> responseFromAPI = response.body();
                List<String>  wards  = new ArrayList<>();
                for (int i = 0; i < responseFromAPI.getData().length; i++) {
                    wards.add(responseFromAPI.getData()[i].get_name()) ;
                }
                adtWards =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, wards);
                spnWards.setAdapter(adtWards);
            }

            @Override
            public void onFailure(Call<ResponseAPI<MWardRes[]>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
