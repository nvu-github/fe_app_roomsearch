package com.example.fe_app_roomsearch.src.model.user.room;

import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.model.user.media.MediaRes;

public class MRoom {
    private Integer id, province, district, ward;
    private String name, description, micro_address, type, created_at;
    private Float price, acreage;
    private Long expired;
    private MediaRes avatar;
    private MFavoriteRes favorite;

    public Integer getId() {
        return id;
    }

    public Integer getProvince() {
        return province;
    }

    public Integer getDistrict() {
        return district;
    }

    public Integer getWard() {
        return ward;
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
}
