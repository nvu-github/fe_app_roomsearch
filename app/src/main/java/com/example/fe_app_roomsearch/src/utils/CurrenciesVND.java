package com.example.fe_app_roomsearch.src.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrenciesVND {
    public static String formatted(String value) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String formattedNumber = currencyFormatter.format(Double.parseDouble(value));
        return formattedNumber;
    }

    public static String roomStatus(String value) {
        String status = "";
        switch (value) {
            case "open":
                status =  "Còn phòng";
                break;
            case "close":
                status = "Đang cho thuê";
                break;
        }

        return status;
    }
}
