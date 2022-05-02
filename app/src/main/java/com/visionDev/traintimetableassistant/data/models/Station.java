package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Station {
    @PrimaryKey
    public  long id;

    @ColumnInfo(name="line_id")
    public long lineId;

    @ColumnInfo
    public String name;

    @ColumnInfo(name="no_of_platforms")
    public int noOfPlatforms;

    @ColumnInfo(name="is_major_station")
    public boolean isMajorStation;
}
