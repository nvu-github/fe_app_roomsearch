package com.example.fe_app_roomsearch.src.item;

import java.util.Date;

public class ItemHomeRoomNew {
    private String key;
    private int urlImage;
    private String title;
    private String price;
    private String address;
    private String time;
    private int favourite;

    public ItemHomeRoomNew(String key, int urlImage, String title, String price, String address, String time, int favourite) {
        this.key = key;
        this.urlImage = urlImage;
        this.title = title;
        this.price = price;
        this.address = address;
        this.time = time;
        this.favourite = favourite;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(int urlImage) {
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
