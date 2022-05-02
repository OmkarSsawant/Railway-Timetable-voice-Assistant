package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Arrival {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public long trainId;

    @ColumnInfo
    public long station_id;

    @ColumnInfo(name="arrival_time")
    public Timestamp arrivalTime;

    @ColumnInfo(name="platform_no")
    public  int platformNumber;

    public Arrival(long trainId, long station_id, Timestamp arrivalTime, int platformNumber) {
        this.trainId = trainId;
        this.station_id = station_id;
        this.arrivalTime = arrivalTime;
        this.platformNumber = platformNumber;
    }
}
