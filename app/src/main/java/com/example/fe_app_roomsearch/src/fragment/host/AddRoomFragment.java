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
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomCreateRes;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomReq;
import com.example.fe_app_roomsearch.src.model.host.room.MRoomUploadReq;
import com.example.fe_app_roomsearch.src.model.location.LocationSpinner;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.service.ILocationService;
import com.example.fe_app_roomsearch.src.service.IRoomService;
import com.example.fe_app_roomsearch.src.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoomFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;

    private ILocationService locationService;
    private IRoomService roomService;

    private Button chooseFileButton;
    private List<String> images;
    private RecyclerView recyclerView;
    private Button btnSave;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Spinner spnProvince, spnDistrict, spnWards, spnTypeRoom, spnRoomStatus;
    private Integer provinceId, districtId, wardId;
    private String typeRoom, roomStatus;
    private ProgressDialog progress;

    private ArrayAdapter<String> adtTypeRoom, adtRoomStatus;
    private ArrayAdapter<LocationSpinner> adtProvince, adtDistrict, adtWards;

    private List<MDistrictRes> districts;
    private List<MWardRes> wards;

    private EditText title, price, acreage, address, description;

    private String[] typeRoomList = {"Chung cư mini", "Phòng trọ", "Nhà ở"};
    private String[] roomStatusList = {"Còn phòng", "Đang cho thuê"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_admin_host_fragment_add_room, container, false);

        initView(view);
        setAdapterDefault();
        new AddRoomFragment.FetchData().execute();

        chooseFileButton.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        return view;
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getProvinces();
            getDistrict();
            getWards();
            spinnerPick();
            return null;
        }
    }


    private void initView(View view) {
        locationService = RetrofitClient.getClient(getContext()).create(ILocationService.class);
        roomService = RetrofitClient.getClient(getContext()).create(IRoomService.class);
        sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        recyclerView = (RecyclerView) view.findViewById(R.id.rcvImageUpload);
        chooseFileButton = (Button) view.findViewById(R.id.btnChooseFile);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        spnProvince = (Spinner) view.findViewById(R.id.province);
        spnDistrict = (Spinner) view.findViewById(R.id.district);
        spnWards = (Spinner) view.findViewById(R.id.wards);
        spnTypeRoom = (Spinner) view.findViewById(R.id.typeRoom);
        spnRoomStatus = (Spinner) view.findViewById(R.id.roomStatus);
        title = (EditText) view.findViewById(R.id.title);
        price = (EditText) view.findViewById(R.id.price);
        acreage = (EditText) view.findViewById(R.id.acreage);
        address = (EditText) view.findViewById(R.id.address);
        description = (EditText) view.findViewById(R.id.description);
    }

    private void setAdapterDefault() {
        adtTypeRoom = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeRoomList);
        adtRoomStatus = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, roomStatusList);

        spnTypeRoom.setAdapter(adtTypeRoom);
        spnRoomStatus.setAdapter(adtRoomStatus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave: {
                progress = ProgressDialog.show(getContext(), "Thêm", "Đang thêm bài đăng phòng...");
                String titleRoom = title.getText().toString();
                String descriptionRoom = description.getText().toString();
                String addressRoom = address.getText().toString();
                String priceRoom = price.getText().toString();
                String acreageRoom = acreage.getText().toString();
                if (
                        titleRoom == null
                                || descriptionRoom == null
                                || addressRoom == null
                                || priceRoom == null
                                || acreageRoom == null
                                || typeRoom == null
                                || provinceId == null
                                || districtId == null
                                || wardId == null
                ) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    return;
                }
                Log.d(TAG, "onClick: save room"+ roomStatus);
                MRoomReq mRoomReq = new MRoomReq(
                        titleRoom,
                        descriptionRoom,
                        addressRoom,
                        typeRoom,
                        roomStatus,
                        provinceId,
                        districtId,
                        wardId,
                        0,
                        Float.parseFloat(priceRoom),
                        Float.parseFloat(acreageRoom)
                );
                roomCreate(mRoomReq);
                uploadMediaRoom(1);
                break;
            }

            case R.id.btnChooseFile: {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return;
                }

                if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, 10);
                }
                break;
            }
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

    private void spinnerPick(){
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LocationSpinner province =(LocationSpinner) parentView.getItemAtPosition(position);
                provinceId = province.tag;
                List<LocationSpinner> districtFilters  = new ArrayList<>();
                if (districts != null) {
                    districtFilters.add(new LocationSpinner("Chọn quận/huyện", null));
                    for (int i = 0; i < districts.size(); i++) {
                        if (province.tag == districts.get(i).get_province_id()) {
                            districtFilters.add(new LocationSpinner(districts.get(i).get_name(), districts.get(i).getId())) ;
                        }
                    }
                    adtDistrict =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtFilters);
                    spnDistrict.setAdapter(adtDistrict);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spnDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LocationSpinner district = (LocationSpinner) parentView.getItemAtPosition(position);
                districtId = district.tag;
                List<LocationSpinner> wardFilters  = new ArrayList<>();
                if (wards != null) {
                    wardFilters.add(new LocationSpinner("Chọn phường/xã", null));
                    for (int i = 0; i < wards.size(); i++) {
                        if (district.tag == wards.get(i).get_district_id()) {
                            wardFilters.add(new LocationSpinner(wards.get(i).get_name(), wards.get(i).getId())) ;
                        }
                    }
                    adtWards =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, wardFilters);
                    spnWards.setAdapter(adtWards);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        spnWards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LocationSpinner ward = (LocationSpinner) adapterView.getItemAtPosition(i);
                wardId = ward.tag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnTypeRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        typeRoom = "motel_room";
                        break;
                    case 1:
                        typeRoom = "apartment";
                        break;
                    case 2:
                        typeRoom = "house";
                        break;
                    default:
                        typeRoom = "apartment";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnRoomStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: room status"+i);
              switch (i){
                  case 0:
                      roomStatus = "open";
                      break;
                  case 1:
                      roomStatus = "close";
                      break;
                  default: roomStatus = "open";
              }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void roomCreate(MRoomReq mRoomReq) {
        Call<ResponseAPI<MRoomCreateRes>> call = roomService.roomCreate(mRoomReq);
        call.enqueue(new Callback<ResponseAPI<MRoomCreateRes>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MRoomCreateRes>> call, Response<ResponseAPI<MRoomCreateRes>> response) {
                if (response.isSuccessful()) {
                    ResponseAPI<MRoomCreateRes> responseFromAPI = response.body();
                    if (responseFromAPI.getStatusCode() == 201 || responseFromAPI.getStatusCode() == 200) {
                        uploadMediaRoom(responseFromAPI.getData().getId());
                    }
                } else {
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progress.dismiss();
                    Log.e("TAG", "Error body: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MRoomCreateRes>> call, Throwable t) {
                progress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void uploadMediaRoom(int roomId) {
        List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            Uri fileUri = Uri.parse(images.get(i));
            File file = new File(FileUtils.getRealPath(getContext(),fileUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(fileUri)), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
            files.add(part);
        }

        Call<ResponseAPI<MRoomUploadReq>> call = roomService.uploadFiles(files, roomId, "content");
        call.enqueue(new Callback<ResponseAPI<MRoomUploadReq>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MRoomUploadReq>> call, Response<ResponseAPI<MRoomUploadReq>> response) {
                if (response.isSuccessful()) {
                    ResponseAPI<MRoomUploadReq> responseFromAPI = response.body();
                    if (responseFromAPI.getStatusCode() == 201 || responseFromAPI.getStatusCode() == 200) {
                        sharedPreferences.edit().clear().apply();
                        progress.dismiss();
                        Toast.makeText(getContext(), "Thêm bài đăng phòng thành công", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        RoomFragment roomFragment = new RoomFragment();
                        fragmentTransaction.replace(R.id.content_frame, roomFragment);
                        fragmentTransaction.commit();
                    }
                } else {
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progress.dismiss();
                    Log.e("TAG", "Error body: " + errorBody);
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MRoomUploadReq>> call, Throwable t) {
                progress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getProvinces(){
        ILocationService locationService = RetrofitClient.getClient(getContext()).create(ILocationService.class);
        Call<ResponseAPI<MProvinceRes[]>> call = locationService.getProvinces();
        call.enqueue(new Callback<ResponseAPI<MProvinceRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MProvinceRes[]>> call, Response<ResponseAPI<MProvinceRes[]>> response) {
                ResponseAPI<MProvinceRes[]> responseFromAPI = response.body();
                List<LocationSpinner>  provinces  = new ArrayList<>();
                provinces.add(new LocationSpinner("Chọn tỉnh/TP", 0));
                for (int i = 0; i < responseFromAPI.getData().length; i++) {
                    provinces.add(new LocationSpinner(responseFromAPI.getData()[i].get_name(), responseFromAPI.getData()[i].getId())) ;
                }
                adtProvince =  new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, provinces);
                spnProvince.setAdapter(adtProvince);
            }

            @Override
            public void onFailure(Call<ResponseAPI<MProvinceRes[]>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getDistrict(){
        Call<ResponseAPI<MDistrictRes[]>> call = locationService.getDistricts();
        call.enqueue(new Callback<ResponseAPI<MDistrictRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MDistrictRes[]>> call, Response<ResponseAPI<MDistrictRes[]>> response) {
                ResponseAPI<MDistrictRes[]> responseFromAPI = response.body();
                districts = new ArrayList<>();
                for (int i = 0; i < responseFromAPI.getData().length; i++) {
                    districts.add(new MDistrictRes(responseFromAPI.getData()[i].getId(), responseFromAPI.getData()[i].get_province_id(), responseFromAPI.getData()[i].get_name(), responseFromAPI.getData()[i].get_prefix())) ;
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MDistrictRes[]>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getWards(){
        ILocationService locationService = RetrofitClient.getClient(getContext()).create(ILocationService.class);
        Call<ResponseAPI<MWardRes[]>> call = locationService.getWards();
        call.enqueue(new Callback<ResponseAPI<MWardRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MWardRes[]>> call, Response<ResponseAPI<MWardRes[]>> response) {
                ResponseAPI<MWardRes[]> responseFromAPI = response.body();
                wards = new ArrayList<>();
                for (int i = 0; i < responseFromAPI.getData().length; i++) {
                    wards.add(new MWardRes(responseFromAPI.getData()[i].getId(), responseFromAPI.getData()[i].get_province_id(), responseFromAPI.getData()[i].get_district_id(), responseFromAPI.getData()[i].get_name(), responseFromAPI.getData()[i].get_prefix()));
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MWardRes[]>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
