package com.visionDev.traintimetableassistant.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface TrainDAO {


    @Query(value = "SELECT * FROM Train")
    Single<List<Train>> getTrains();

    @Query(value = "SELECT name FROM Line WHERE id = :line_id")
    Single<String> getLineName( long line_id);

    @Query(value = "SELECT * FROM Arrival WHERE trainId = :train_id ORDER BY DATE(arrival_time)")
    Single<List<Arrival>> getArrivals(long train_id);


    @Query(value = "SELECT name FROM station WHERE station_no=:stationNo")
    String getStationName(long stationNo);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addStation(Station s);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addTrain(Train t);

    @Update
    Single<Integer> updateTrain(Train t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addLine(Line l);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addArrival(Arrival ms);


    @Query(value = "SELECT * FROM Station")
    Single<List<Station>> getStations();


}
