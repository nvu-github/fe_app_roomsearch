package com.example.fe_app_roomsearch.src.model.host.room;


public class MRoomReq {
    private String name, description, micro_address, type, status;
    private Integer province, district, ward, expired;
    private Float price, acreage;

    public MRoomReq(String name, String description, String micro_address, String type, String status, Integer province, Integer district, Integer ward, Integer expired, Float price, Float acreage) {
        this.name = name;
        this.description = description;
        this.micro_address = micro_address;
        this.type = type;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getExpired() {
        return expired;
    }

    public void setExpired(Integer expired) {
        this.expired = expired;
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
