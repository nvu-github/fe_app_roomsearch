package com.example.fe_app_roomsearch.src.fragment.user;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.CategoryAdapter;
//import com.example.fe_app_roomsearch.src.adapter.FavoriteAdapter;
import com.example.fe_app_roomsearch.src.adapter.FavoriteAdapter;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.helper.TimeHelper;
import com.example.fe_app_roomsearch.src.item.ItemCategory;
import com.example.fe_app_roomsearch.src.item.ItemHomeRoomNew;
import com.example.fe_app_roomsearch.src.model.MPaginate;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.favorite.MFavorite;
import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteResPag;
import com.example.fe_app_roomsearch.src.service.IFavoriteService;
import com.example.fe_app_roomsearch.src.utils.CurrenciesVND;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment {
    private RecyclerView favoriteList;

    private FavoriteAdapter favoriteAdapter;

    private RecyclerView listCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new FetchFavorite();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_user_fragment_favourite, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);

        listCategory = view.findViewById(R.id.favoriteList);
        favoriteAdapter = new FavoriteAdapter(getActivity());

        listCategory.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        listCategory.addItemDecoration(itemDecoration);

        getFavoriteRooms();

        return view;
    }

    private class FetchFavorite extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getFavoriteRooms();
            return null;
        }
    }

    private void getFavoriteRooms(){
        SharedPreferences prefs = this.getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        if (!prefs.contains("accessToken") || prefs.getString("accessToken" , "accessToken") == "") {
            return;
        }
        String accessToken = prefs.getString("accessToken" , "accessToken");

        IFavoriteService favoriteService = RetrofitClient.getClient(getContext()).create(IFavoriteService.class);

        Call<ResponseAPI<MFavoriteResPag>> call = favoriteService.favorites(getResources().getString(R.string.token_type)+" "+accessToken, 1, 1000);
        // on below line we are executing our method.
        call.enqueue(new Callback<ResponseAPI<MFavoriteResPag>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MFavoriteResPag>> call, Response<ResponseAPI<MFavoriteResPag>> response) {
                ResponseAPI<MFavoriteResPag> responseFromAPI = response.body();
                if (responseFromAPI != null) {
                    ArrayList<MFavorite> favorites = responseFromAPI.getData().getItems();
                    MPaginate meta = responseFromAPI.getData().getMeta();

                    List<ItemCategory> roomList = new ArrayList<>();
                    List<ItemHomeRoomNew> favoriteRooms = new ArrayList<>();
                    for (int i = 0; i < favorites.size(); i++) {
                        MFavorite favorite = favorites.get(i);
                        String avatar = "nothing";

                        if(favorite.getRoom().getAvatar() != null){
                            avatar = getResources().getString(R.string.urlMedia) + favorite.getRoom().getAvatar().getUrl();
                        }
                        favoriteRooms.add(new ItemHomeRoomNew(
                                String.valueOf(favorite.getRoom().getId()),
                                avatar,
                                favorite.getRoom().getName(),
                                CurrenciesVND.formatted(favorite.getRoom().getPrice().toString())+"/tháng",
                                favorite.getRoom().getMicro_address(),
                                favorite.getRoom().getCreated_at(),
                                favorite.getRoom().getStatus(),
                                R.drawable.ic_card_favourite
                        ));
                    }
                    roomList.add(new ItemCategory("Danh sách yêu thích", favoriteRooms));
                    favoriteAdapter.setData(roomList);
                    listCategory.setAdapter((favoriteAdapter));
                }
            }

            @Override
            public void onFailure(Call<ResponseAPI<MFavoriteResPag>> call, Throwable t) {
                // setting text to our text view when
                // we get error response from API.
                Log.d(TAG, "onFailure: favorite fail" + t.getMessage());
            }


        });
    }
}
