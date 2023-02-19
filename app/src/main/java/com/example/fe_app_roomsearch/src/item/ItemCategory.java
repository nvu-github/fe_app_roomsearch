package com.example.fe_app_roomsearch.src.item;

import java.util.List;

public class ItemCategory {
    private String nameCategory;
    private List<ItemHomeRoomNew> itemCategory;

    public ItemCategory(String nameCategory, List<ItemHomeRoomNew> itemCategory) {
        this.nameCategory = nameCategory;
        this.itemCategory = itemCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<ItemHomeRoomNew> getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(List<ItemHomeRoomNew> itemCategory) {
        this.itemCategory = itemCategory;
    }
}
