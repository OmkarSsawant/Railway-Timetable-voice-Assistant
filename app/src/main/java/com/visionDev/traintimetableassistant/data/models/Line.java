package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Line {
    @PrimaryKey
    int id;

    @ColumnInfo
    String name;

}
