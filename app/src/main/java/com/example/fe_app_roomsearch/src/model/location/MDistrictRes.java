package com.example.fe_app_roomsearch.src.model.location;

public class MDistrictRes {
    private Integer id, _province_id;
    private String _name, _prefix;

    public MDistrictRes(Integer id, Integer _province_id, String _name, String _prefix) {
        this.id = id;
        this._province_id = _province_id;
        this._name = _name;
        this._prefix = _prefix;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer get_province_id() {
        return _province_id;
    }

    public void set_province_id(Integer _province_id) {
        this._province_id = _province_id;
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
}
