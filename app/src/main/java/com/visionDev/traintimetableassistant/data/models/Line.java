package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Line implements Serializable {
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String name;

    public Line(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
