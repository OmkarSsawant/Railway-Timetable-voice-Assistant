package com.visionDev.traintimetableassistant.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface TrainDAO {

    @Query(value = "SELECT * FROM Train")
    Single<List<Train>> getTrains();

    @Query(value = "SELECT * FROM Train")
    Flowable<List<Train>> observeTrains();


    @Query(value = "SELECT * FROM Line")
    Flowable<List<Line>> observeLines();


    @Query(value = "SELECT * FROM Arrival WHERE trainId = :train_id ORDER BY DATE(arrival_time)")
    Single<List<Arrival>> getArrivals(long train_id);


    @Query(value = "SELECT name FROM station WHERE station_no=:stationNo")
    String getStationName(long stationNo);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addStation(Station s);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addTrain(Train t);


    @Query(value = "SELECT station_no FROM station WHERE name=:stationName LIMIT 1")
    Long getIdOfStation(String stationName);

    @Update
    Single<Integer> updateTrain(Train t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addLine(Line l);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> addArrival(Arrival ms);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Void> addArrivals(List<Arrival> ms);


    @Delete
    void deleteTrain(Train t);

    @Delete
    int deleteStation(Station s);

    @Query(value = "SELECT * FROM Station")
    List<Station> getStations();

    @Query(value = "SELECT * FROM Station")
    Flowable<List<Station>> observeStations();

    @Query(value = "SELECT name FROM station")
    List<String> getStationNames();
}
