package com.example.fe_app_roomsearch.src.fragment.user;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.ImageUploadAdapter;
import com.example.fe_app_roomsearch.src.adapter.user.SearchAdapter;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.location.LocationSpinner;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.service.ILocationService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private ILocationService locationService;
    private SearchAdapter searchAdapter;
    private List<ItemHomeRoomNew> mListItemHomeRoomNews;
    private RecyclerView listItemRoom;
    private Spinner spnProvince, spnDistrict, spnWards, typeRoom;

    private ArrayAdapter<String> adtTypeRoom;
    private ArrayAdapter<LocationSpinner> adtProvince, adtDistrict, adtWards;

    private List<MDistrictRes> districts;
    private List<MWardRes> wards;

    private String[] typeRoomList = {"Chung cư mini", "Phòng trọ", "Nhà ở"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_fragment_search, container, false);

        locationService = RetrofitClient.getClient(getContext()).create(ILocationService.class);

        spnProvince = (Spinner) view.findViewById(R.id.province);
        spnDistrict = (Spinner) view.findViewById(R.id.district);
        spnWards = (Spinner) view.findViewById(R.id.wards);
        typeRoom = (Spinner) view.findViewById(R.id.typeRoom);
        listItemRoom = (RecyclerView) view.findViewById(R.id.listItemRoom);

        mListItemHomeRoomNews = itemHomeRoomNews();

        adtTypeRoom = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeRoomList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listItemRoom.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(getContext(), mListItemHomeRoomNews);

        listItemRoom.setAdapter(searchAdapter);
        typeRoom.setAdapter(adtTypeRoom);

        new FetchData().execute();

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

    private List<ItemHomeRoomNew> itemHomeRoomNews() {
        List<ItemHomeRoomNew> roomNews = new ArrayList<>();
        roomNews.add(new ItemHomeRoomNew("1","https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg", "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        roomNews.add(new ItemHomeRoomNew("2","https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg", "Phòng trọ khu vực Định Công","2.000.000 đ/tháng","Ngõ 175, Phường Định Công, Quận Hoàng Mai, TP.Hà Nội", "09/02/2022 15:12", R.drawable.ic_card_favourite));
        return roomNews;
    }

    private void spinnerPick(){
        spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LocationSpinner province =(LocationSpinner) parentView.getItemAtPosition(position);
                List<LocationSpinner> districtFilters  = new ArrayList<>();
                if (districts != null) {
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
                List<LocationSpinner> wardFilters  = new ArrayList<>();
                if (wards != null) {
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
    }

    private void getProvinces(){
        Call<ResponseAPI<MProvinceRes[]>> call = locationService.getProvinces();
        call.enqueue(new Callback<ResponseAPI<MProvinceRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MProvinceRes[]>> call, Response<ResponseAPI<MProvinceRes[]>> response) {

                ResponseAPI<MProvinceRes[]> responseFromAPI = response.body();
                List<LocationSpinner>  provinces  = new ArrayList<>();
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
