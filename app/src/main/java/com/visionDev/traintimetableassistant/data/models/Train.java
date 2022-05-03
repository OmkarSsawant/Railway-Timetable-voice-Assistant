package com.visionDev.traintimetableassistant.data.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.visionDev.traintimetableassistant.data.room.TrainDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Entity
public class Train implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Long id;

    @ColumnInfo(name="start_station_id")
    public long startStationId;

    @ColumnInfo(name="end_station_id")
    public long endStationId;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="is_fast")
    public boolean isFastTrain;

    @Ignore
    public List<Arrival> arrivals;

    public Train(Long id, String name,long startStationId, long endStationId, boolean isFastTrain) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.isFastTrain = isFastTrain;
        this.name = name;
    }

    @Ignore
    public Observable<List<Arrival>> loadArrivals(TrainDAO dao){
        Single<List<Arrival>> arrivals = dao.getArrivals(id);
        arrivals.blockingSubscribe(ar->{
            Log.i("TRAIN : " , id + " " + ar.size());
        });
        return        arrivals.toObservable();
    }

    @Ignore
    public  boolean isInRoute(TrainDAO dao,String start,String end){
        Log.i("DEBUG -- ARRIVALS", arrivals.size() + "");
        ArrayList<String> midStations = new ArrayList<>();
        for (Arrival m: arrivals) {
            try{
                midStations.add(dao.getStationName(m.station_id));
            }catch (Exception e){
                Log.e("ERROR for "+ m.station_id,e.getLocalizedMessage());
            }
        }

        for (String m:
             midStations) {
            Log.i("TRAIN", name + " =>  " + m);
        }
        Log.i("TRAIN" , start + "->"+ midStations.indexOf(start) + " < " + end + "->" + midStations.indexOf(end) +(midStations.indexOf(start) < midStations.indexOf(end)));
        return midStations.indexOf(start) < midStations.indexOf(end);
    }
}
