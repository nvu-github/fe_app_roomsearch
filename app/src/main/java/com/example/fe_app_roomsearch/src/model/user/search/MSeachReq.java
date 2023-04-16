package com.example.fe_app_roomsearch.src.model.user.search;

public class MSeachReq {
    private Integer provinceId, districtId, wardId;
    private String typeRoom;

    public MSeachReq(Integer provinceId, Integer districtId, Integer wardId, String typeRoom) {
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.wardId = wardId;
        this.typeRoom = typeRoom;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public String getTypeRoom() {
        return typeRoom;
    }
}
