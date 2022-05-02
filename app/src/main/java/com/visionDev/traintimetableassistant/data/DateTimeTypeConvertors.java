package com.visionDev.traintimetableassistant.data;

import androidx.room.TypeConverter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public  class DateTimeTypeConvertors {

    final DateFormat  df = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US);

    @TypeConverter
    public Timestamp getDateTime(String date){

        try {
            return  new Timestamp(df.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
