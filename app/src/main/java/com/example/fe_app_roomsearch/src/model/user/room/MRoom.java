package com.example.fe_app_roomsearch.src.model.user.room;

import com.example.fe_app_roomsearch.src.model.favorite.MFavoriteRes;
import com.example.fe_app_roomsearch.src.model.location.MDistrictRes;
import com.example.fe_app_roomsearch.src.model.location.MProvinceRes;
import com.example.fe_app_roomsearch.src.model.location.MWardRes;
import com.example.fe_app_roomsearch.src.model.user.MUserRes;
import com.example.fe_app_roomsearch.src.model.user.media.MediaRes;

public class MRoom {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MProvinceRes getProvince() {
        return province;
    }

    public void setProvince(MProvinceRes province) {
        this.province = province;
    }

    public MDistrictRes getDistrict() {
        return district;
    }

    public void setDistrict(MDistrictRes district) {
        this.district = district;
    }

    public MWardRes getWard() {
        return ward;
    }

    public void setWard(MWardRes ward) {
        this.ward = ward;
    }

    public MUserRes getUser() {
        return user;
    }

    public void setUser(MUserRes user) {
        this.user = user;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    public MediaRes getAvatar() {
        return avatar;
    }

    public void setAvatar(MediaRes avatar) {
        this.avatar = avatar;
    }

    public MFavoriteRes getFavorite() {
        return favorite;
    }

    public void setFavorite(MFavoriteRes favorite) {
        this.favorite = favorite;
    }
}
