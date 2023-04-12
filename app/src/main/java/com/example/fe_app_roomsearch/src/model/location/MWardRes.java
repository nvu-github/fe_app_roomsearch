package com.example.fe_app_roomsearch.src.model.location;

public class MWardRes {
    private Integer id, _province_id, _district_id;
    private String _name, _prefix;

    public MWardRes(Integer id, Integer _province_id, Integer _district_id, String _name, String _prefix) {
        this.id = id;
        this._province_id = _province_id;
        this._district_id = _district_id;
        this._name = _name;
        this._prefix = _prefix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_prefix() {
        return _prefix;
    }

    public void set_prefix(String _prefix) {
        this._prefix = _prefix;
    }

    public Integer get_province_id() {
        return _province_id;
    }

    public void set_province_id(Integer _province_id) {
        this._province_id = _province_id;
    }

    public Integer get_district_id() {
        return _district_id;
    }

    public void set_district_id(Integer _district_id) {
        this._district_id = _district_id;
    }
}
