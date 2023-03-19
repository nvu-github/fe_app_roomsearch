package com.example.fe_app_roomsearch.src.helper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {
    private Long timestamp;
    private String date;

    public TimeHelper(Long timestamp) {
        this.timestamp = timestamp;
        this.date = date;
    }

    public String timestampToDate(){
        Date date = new Date(this.timestamp * 1000L); // convert timestamp to Date object
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // create formatter with desired format
       return sdf.format(date);
    }

    public Long dateToTimestamp() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // create formatter with input format
        Date date = sdf.parse(this.date); // parse date string into Date object
        return  date.getTime() / 1000L; //
    }

}
