package com.example.fe_app_roomsearch.src.model.host.room;

import com.google.gson.annotations.Expose;

public class MRoomReq {
    private String name, description, microAddress, type;
    private Integer province, district, ward, expired;
    private Float price, acreage;

    public MRoomReq(String name, String description, String microAddress, String type, Integer province, Integer district, Integer ward, Integer expired, Float price, Float acreage) {
        this.name = name;
        this.description = description;
        this.microAddress = microAddress;
        this.type = type;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.expired = expired;
        this.price = price;
        this.acreage = acreage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMicroAddress() {
        return microAddress;
    }

    public String getType() {
        return type;
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

    public Integer getExpired() {
        return expired;
    }

    public Float getPrice() {
        return price;
    }

    public Float getAcreage() {
        return acreage;
    }
}
