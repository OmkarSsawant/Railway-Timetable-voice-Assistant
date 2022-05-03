package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Station implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="station_no")
    public long stationNo;



    @ColumnInfo(name="line_id")
    public long lineId;

    @ColumnInfo
    public String name;

    @ColumnInfo(name="no_of_platforms")
    public int noOfPlatforms;

    @ColumnInfo(name="is_major_station")
    public boolean isMajorStation;

    public Station(long stationNo, long lineId, String name, int noOfPlatforms, boolean isMajorStation) {
        this.stationNo = stationNo;
        this.lineId = lineId;
        this.name = name;
        this.noOfPlatforms = noOfPlatforms;
        this.isMajorStation = isMajorStation;
    }
}
