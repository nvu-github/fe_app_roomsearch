package com.example.fe_app_roomsearch.src.fragment.user;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.user.SearchAdapter;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.location.LocationSpinner;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
import com.example.fe_app_roomsearch.src.model.user.search.MSeachReq;
import com.example.fe_app_roomsearch.src.service.ILocationService;
import com.example.fe_app_roomsearch.src.service.user.ISearchService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private ILocationService locationService;
    private ISearchService searchService;

    private SearchAdapter searchAdapter;
    private List<ItemHomeRoomNew> mListItemSearch;
    private RecyclerView listItemRoom;
    private Spinner spnProvince, spnDistrict, spnWards, spnTypeRoom;
    private Button btnSearch;
    private Integer provinceId, districtId, wardId;
    private String typeRoom;
    private ProgressDialog progress;

    private ArrayAdapter<String> adtTypeRoom;
    private ArrayAdapter<LocationSpinner> adtProvince, adtDistrict, adtWards;

    private List<MDistrictRes> districts;
    private List<MWardRes> wards;

    private String[] typeRoomList = {"Chọn loại phòng", "Phòng trọ", "Chung cư mini", "Nhà ở"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_fragment_search, container, false);
        initView(view);
        setAdapterDefault();
        new FetchData().execute();
        btnSearch.setOnClickListener((View.OnClickListener) this);
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
        searchService = RetrofitClient.getClient(getContext()).create(ISearchService.class);
        spnProvince = (Spinner) view.findViewById(R.id.province);
        spnDistrict = (Spinner) view.findViewById(R.id.district);
        spnWards = (Spinner) view.findViewById(R.id.wards);
        spnTypeRoom = (Spinner) view.findViewById(R.id.typeRoom);
        listItemRoom = (RecyclerView) view.findViewById(R.id.listItemRoom);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch: {
                progress = ProgressDialog.show(getContext(), "Tìm kiếm", "Đang tìm kiếm...");
                searchRoom(new MSeachReq(provinceId, districtId, wardId, typeRoom));
            }
        }
    }

    private void setAdapterDefault() {
        adtTypeRoom = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, typeRoomList);
        spnTypeRoom.setAdapter(adtTypeRoom);
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
                            wardFilters.add(new LocationSpinner(wards.get(i).get_name(), wards.get(i).getId()));
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
                if (i == 0) {
                    typeRoom = null;
                } else if (i == 1) {
                    typeRoom = "motel_room";
                } else if (i == 2) {
                    typeRoom = "apartment";
                } else if (i == 3) {
                    typeRoom = "house";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<ItemHomeRoomNew> itemRoomSearch(ArrayList<MRoom> rooms) {
        List<ItemHomeRoomNew> roomSearches = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            int favouriteIcon = R.drawable.ic_card_favourite_none;
            MRoom room = rooms.get(i);
            String avatar = "https://znews-photo.zingcdn.me/w660/Uploaded/lce_jwqqc/2023_01_11/FF4lj5_XIAAPCn1_1.jpg";
            if(room.getAvatar() != null){
                avatar = getResources().getString(R.string.urlMedia) + room.getAvatar().getUrl();
            }
            if(room.getFavorite() != null) {
                favouriteIcon = R.drawable.ic_card_favourite;
            }
            roomSearches.add(new ItemHomeRoomNew(
                    String.valueOf(rooms.get(i).getId()),avatar,
                    rooms.get(i).getName(),
                    rooms.get(i).getPrice().toString()+"đ/tháng",
                    rooms.get(i).getMicro_address() + rooms.get(i).getAddress(),
                    rooms.get(i).getCreated_at(),
                    favouriteIcon
            ));
        }
        return roomSearches;
    }

    private void searchRoom(MSeachReq seachReq){
        Call<ResponseAPI<ArrayList<MRoom>>> call = searchService.searchRoom(
                seachReq.getProvinceId(),
                seachReq.getDistrictId(),
                seachReq.getWardId(),
                seachReq.getTypeRoom()
        );
        call.enqueue(new Callback<ResponseAPI<ArrayList<MRoom>>>() {
            @Override
            public void onResponse(Call<ResponseAPI<ArrayList<MRoom>>> call, Response<ResponseAPI<ArrayList<MRoom>>> response) {
                progress.dismiss();
                if (response.isSuccessful()) {
                    ResponseAPI<ArrayList<MRoom>> responseFromAPI = response.body();
                    ArrayList<MRoom> rooms = responseFromAPI.getData();
                    mListItemSearch = itemRoomSearch(rooms);
                    searchAdapter = new SearchAdapter(getContext(), mListItemSearch);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    listItemRoom.setLayoutManager(layoutManager);
                    listItemRoom.setAdapter(searchAdapter);
                }

            }

            @Override
            public void onFailure(Call<ResponseAPI<ArrayList<MRoom>>> call, Throwable t) {
                progress.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }


        });
    }

    private void getProvinces(){
        Call<ResponseAPI<MProvinceRes[]>> call = locationService.getProvinces();
        call.enqueue(new Callback<ResponseAPI<MProvinceRes[]>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MProvinceRes[]>> call, Response<ResponseAPI<MProvinceRes[]>> response) {
                ResponseAPI<MProvinceRes[]> responseFromAPI = response.body();
                List<LocationSpinner> provinces = new ArrayList<>();
                provinces.add(new LocationSpinner("Chọn tỉnh/TP", null));
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
