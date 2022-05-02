package com.visionDev.traintimetableassistant.data.doa;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.visionDev.traintimetableassistant.data.DateTimeTypeConvertors;
import com.visionDev.traintimetableassistant.data.models.MidStation;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.util.List;

@Dao
public interface TrainDAO {


    @Query(value = "SELECT * FROM Train")
    List<Train> getTrains();

    @Query(value = "SELECT name FROM Line WHERE id = :line_id")
    String getLineName( long line_id);

    @Query(value = "SELECT * FROM MidStation WHERE id = :train_id ORDER BY DATE(arrival_time)")
    @TypeConverters(value = {DateTimeTypeConvertors.class})
    List<MidStation> getMidStationsOfTrain(long train_id);


    @Query(value = "SELECT name FROM station WHERE id=:stationId")
    String getStationName(long stationId);


}
