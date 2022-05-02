package com.visionDev.traintimetableassistant.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.visionDev.traintimetableassistant.data.doa.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.*;

@Database(
        entities = {Line.class, Station.class, Arrival.class, Train.class},
        version = 1
)
@TypeConverters(value = {DateTimeTypeConvertors.class})

public abstract class TrainTimeTableDB extends RoomDatabase {
   public abstract TrainDAO getTrainDAO();
}
