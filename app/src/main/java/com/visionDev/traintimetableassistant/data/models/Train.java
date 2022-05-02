package com.visionDev.traintimetableassistant.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.visionDev.traintimetableassistant.data.doa.TrainDAO;

import java.util.List;

@Entity
public class Train {

    @PrimaryKey
    public long id;

    @ColumnInfo(name="start_station_id")
    public long startStationId;

    @ColumnInfo(name="end_station_id")
    public long endStationId;

    @ColumnInfo(name = "midstations")
    public String midStations;

    @ColumnInfo(name="is_fast")
    public boolean isFastTrain;

    @Ignore
    private List<MidStation> mMidStations;

    @Ignore
    public void loadMidStations(TrainDAO dao){
        mMidStations = dao.getMidStationsOfTrain(id);
    }

    @Ignore
    public  boolean isInRoute(TrainDAO dao,String start,String end){
        boolean hasStartPlace = false;

        for (MidStation m: mMidStations) {
                if(dao.getStationName(m.station_id).equals(start)){
                    hasStartPlace = true;
                }
                if(hasStartPlace && dao.getStationName(m.station_id).equals(end)){
                    return  true;
                }
        }
        return  false;
    }
}
