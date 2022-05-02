package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class MidStation {
    @PrimaryKey
    public long id;

    @ColumnInfo
    public long station_id;

    @ColumnInfo(name="arrival_time")
    public Timestamp arrivalTime;

    @ColumnInfo(name="platform_no")
    public  int platformNumber;

}
