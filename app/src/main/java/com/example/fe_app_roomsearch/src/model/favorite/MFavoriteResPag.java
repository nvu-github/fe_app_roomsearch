package com.example.fe_app_roomsearch.src.model.favorite;

import com.example.fe_app_roomsearch.src.model.MPaginate;
<<<<<<< HEAD
=======
import com.example.fe_app_roomsearch.src.model.user.room.MRoom;
>>>>>>> 6ddc834 (add screen profile and logout)

import java.util.ArrayList;

public class MFavoriteResPag {
    private ArrayList<MFavorite> items;
    private MPaginate meta;

    public ArrayList<MFavorite> getItems() {
        return items;
    }

    public void setItems(ArrayList<MFavorite> items) {
        this.items = items;
    }

    public MPaginate getMeta() {
        return meta;
    }

    public void setMeta(MPaginate meta) {
        this.meta = meta;
    }
}
