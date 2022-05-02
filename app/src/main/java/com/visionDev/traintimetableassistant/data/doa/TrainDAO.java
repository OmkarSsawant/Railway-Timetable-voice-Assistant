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

    @Query(value = "SELECT * FROM Arrival WHERE id = :train_id ORDER BY DATE(arrival_time)")
    @TypeConverters(value = {DateTimeTypeConvertors.class})
    List<Arrival> getMidStationsOfTrain(long train_id);


    @Query(value = "SELECT name FROM station WHERE id=:stationId")
    String getStationName(long stationId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    boolean addStation(Station s);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    boolean addTrain(Train t);

    @Update
    boolean updateTrain(Train t);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    boolean addLine(Line l);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    boolean addMidStation(Arrival ms);




}
