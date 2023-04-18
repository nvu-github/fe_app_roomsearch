package com.example.fe_app_roomsearch.src.item.host;

public class ItemRoom {
    private String key;
    private String avatar;
    private String title;
    private String price;
    private String time;

    public ItemRoom(String key, String avatar, String title, String price, String time) {
        this.key = key;
        this.avatar = avatar;
        this.title = title;
        this.price = price;
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
