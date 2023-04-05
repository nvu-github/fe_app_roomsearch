package com.example.fe_app_roomsearch.src.model.room;

import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.model.media.MediaRes;

public class MRoom {
    private Integer id, province, district, ward;
    private Long expired;

    private MediaRes avatar;

    private MFavoriteRes favorite;

    public MFavoriteRes getFavorite() {
        return favorite;
    }

    public void setFavorite(MFavoriteRes favorite) {
        this.favorite = favorite;
    }

    public MediaRes getAvatar() {
        return avatar;
    }

    public void setAvatar(MediaRes avatar) {
        this.avatar = avatar;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    private String name, description, micro_address, type;
    private Float price, acreage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getWard() {
        return ward;
    }

    public void setWard(Integer ward) {
        this.ward = ward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMicro_address() {
        return micro_address;
    }

    public void setMicro_address(String micro_address) {
        this.micro_address = micro_address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getAcreage() {
        return acreage;
    }

    public void setAcreage(Float acreage) {
        this.acreage = acreage;
    }


}
