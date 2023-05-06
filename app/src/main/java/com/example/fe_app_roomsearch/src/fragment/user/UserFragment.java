package com.example.fe_app_roomsearch.src.fragment.user;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.MainActivity;
import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.adapter.ProfileAdapter;
import com.example.fe_app_roomsearch.src.auth.Login;
import com.example.fe_app_roomsearch.src.config.RetrofitClient;
import com.example.fe_app_roomsearch.src.model.ResponseAPI;
import com.example.fe_app_roomsearch.src.model.user.MUserRes;
import com.example.fe_app_roomsearch.src.service.IUserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {
    private Button btnAuth;
    private IUserService userService;
    private MUserRes profile;
    private ProfileAdapter profileAdapter;
    private RecyclerView rcvProfile;

    private SharedPreferences prefs;

    private BottomNavigationView menuLayout;
    public UserFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user_fragment_user, container, false);
        prefs = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        btnAuth = view.findViewById(R.id.auth);
        rcvProfile = view.findViewById(R.id.profile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rcvProfile.setLayoutManager(linearLayoutManager);

        profileAdapter = new ProfileAdapter(getActivity());

        boolean isLogged = this.isLogged();

        if(isLogged && prefs.getString("accessToken" , "accessToken") != ""){
            btnAuth.setText("logout");
            btnAuth.setTag(R.string.logged, true);
            getProfile();
        }else{
            btnAuth.setTag(R.string.logged, false);
        }
        authentication();
        return view;
    }

    private Boolean isLogged(){
        Long refreshTokenExpires = prefs.getLong("refreshTokenExpires", -1);
        if (refreshTokenExpires > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    private void authentication(){
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLogged = (boolean) btnAuth.getTag(R.string.logged);
                Log.d(TAG, "onClick: logout button " + isLogged);
                if(isLogged){ // if it's true. it's logging. we should logout when click button
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                    editor.remove("accessToken").commit();
                    editor.remove("refreshToken").commit();
                    editor.remove("refreshTokenExpires").commit();
                    editor.remove("accessTokenExpires").commit();
                    btnAuth.setText("login");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getProfile(){
        userService = RetrofitClient.getClient(getContext()).create(IUserService.class);
        Call<ResponseAPI<MUserRes>> call = userService.getProfile();
        call.enqueue(new Callback<ResponseAPI<MUserRes>>() {
            @Override
            public void onResponse(Call<ResponseAPI<MUserRes>> call, Response<ResponseAPI<MUserRes>> response) {
                if(response.body() == null){
                    return;
                }
                profile = response.body().getData();
                profileAdapter.setData(profile);
                rcvProfile.setAdapter((profileAdapter));
            }

            @Override
            public void onFailure(Call<ResponseAPI<MUserRes>> call, Throwable t) {

            }
        });
    }
}
