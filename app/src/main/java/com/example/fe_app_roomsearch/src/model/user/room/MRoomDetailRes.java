package com.example.fe_app_roomsearch.src.model.user.room;

import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.model.user.MUserRes;
import com.example.fe_app_roomsearch.src.model.user.media.MediaRes;

import java.util.List;

public class MRoomDetailRes {
    private Integer id;
    private MProvinceRes province;
    private MDistrictRes district;
    private MWardRes ward;
    private MUserRes user;
    private String name, description, micro_address, type, created_at, address, status;
    private Float price, acreage;
    private Long expired;
    private MediaRes avatar;
    private MFavoriteRes favorite;
    private MUserRes userRes;
    private List<MediaRes> medias;

    public Integer getId() {
        return id;
    }

    public MProvinceRes getProvince() {
        return province;
    }

    public MDistrictRes getDistrict() {
        return district;
    }

    public MWardRes getWard() {
        return ward;
    }

    public MUserRes getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMicro_address() {
        return micro_address;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public Float getPrice() {
        return price;
    }

    public Float getAcreage() {
        return acreage;
    }

    public Long getExpired() {
        return expired;
    }

    public MediaRes getAvatar() {
        return avatar;
    }

    public MFavoriteRes getFavorite() {
        return favorite;
    }

    public List<MediaRes> getMedias() {
        return medias;
    }

    public MUserRes getUserRes() {
        return userRes;
    }
}
