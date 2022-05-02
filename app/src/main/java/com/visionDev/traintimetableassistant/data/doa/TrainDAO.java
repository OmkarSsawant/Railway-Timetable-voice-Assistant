package com.visionDev.traintimetableassistant.data.doa;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.visionDev.traintimetableassistant.data.DateTimeTypeConvertors;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.util.List;

@Dao
public interface TrainDAO {


    @Query(value = "SELECT * FROM Train")
    List<Train> getTrains();

    @Query(value = "SELECT name FROM Line WHERE id = :line_id")
    String getLineName( long line_id);

    @Query(value = "SELECT * FROM Arrival WHERE trainId = :train_id ORDER BY DATE(arrival_time)")
    List<Arrival> getMidStationsOfTrain(long train_id);


    @Query(value = "SELECT name FROM station WHERE id=:stationId")
    String getStationName(long stationId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addStation(Station s);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addTrain(Train t);

    @Update
    int updateTrain(Train t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addLine(Line l);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addArrival(Arrival ms);




}
