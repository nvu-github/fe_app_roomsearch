package com.example.fe_app_roomsearch.src.item;

public class ItemBanner {
    private int resourceId;
    private int keyimage;
    private String url;
    private boolean isMedia;

    public ItemBanner(int resourceId, int keyimage, String url, boolean isMedia) {
        this.resourceId = resourceId;
        this.keyimage = keyimage;
        this.url = url;
        this.isMedia = isMedia;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getKeyimage() {
        return keyimage;
    }

    public void setKeyimage(int keyimage) {
        this.keyimage = keyimage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMedia() {
        return isMedia;
    }

    public void setMedia(boolean media) {
        isMedia = media;
    }
}
