package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Station {
    @PrimaryKey(autoGenerate = true)
    public  long id;

    @ColumnInfo(name="line_id")
    public long lineId;

    @ColumnInfo
    public String name;

    @ColumnInfo(name="no_of_platforms")
    public int noOfPlatforms;

    @ColumnInfo(name="is_major_station")
    public boolean isMajorStation;

    public Station(long id, long lineId, String name, int noOfPlatforms, boolean isMajorStation) {
        this.id = id;
        this.lineId = lineId;
        this.name = name;
        this.noOfPlatforms = noOfPlatforms;
        this.isMajorStation = isMajorStation;
    }
}
