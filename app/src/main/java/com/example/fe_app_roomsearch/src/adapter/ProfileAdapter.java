package com.example.fe_app_roomsearch.src.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fe_app_roomsearch.R;
import com.example.fe_app_roomsearch.src.helper.TimeHelper;
import com.example.fe_app_roomsearch.src.model.user.MUserRes;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private Context mContext;
    private MUserRes profile;

    public void setData(MUserRes userInfo){
        this.profile = userInfo;
        notifyDataSetChanged();
    }

    public ProfileAdapter(Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile,parent,false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileViewHolder holder, int position) {
        TimeHelper timehelper = new TimeHelper(this.profile.getDateOfBirth());
        holder.tvSuperName.setText(this.profile.getFullName());
        holder.tvName.setText(this.profile.getFullName());
        holder.tvPhone.setText(this.profile.getPhone());
        holder.tvDateOfBirth.setText( timehelper.timestampToDate());
        holder.tvAddress.setText(this.profile.getAddress());
    }

    @Override
    public int getItemCount() {
        if(this.profile != null){
            return 1;
        }
        return 0;
    }



    public class ProfileViewHolder extends RecyclerView.ViewHolder{

        private ImageView imvAvatar;
        private TextView tvSuperName, tvName, tvAddress, tvPhone, tvDateOfBirth;
        private Button btnLogout;
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            imvAvatar = itemView.findViewById(R.id.avatar);
            tvSuperName = itemView.findViewById(R.id.superName);
            tvName = itemView.findViewById(R.id.name);
            tvPhone = itemView.findViewById(R.id.phone);
            tvAddress = itemView.findViewById(R.id.address);
            tvDateOfBirth = itemView.findViewById(R.id.dateOfBirth);
        }
    }

}

