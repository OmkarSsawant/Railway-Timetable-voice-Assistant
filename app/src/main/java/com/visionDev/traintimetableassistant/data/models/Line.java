package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Line {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String name;

    public Line(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
