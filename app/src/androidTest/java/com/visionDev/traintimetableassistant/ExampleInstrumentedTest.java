package com.visionDev.traintimetableassistant;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.visionDev.traintimetableassistant.data.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.data.doa.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.visionDev.traintimetableassistant", appContext.getPackageName());
    }

    List<Train> getAvailableTrains(TrainDAO dao,String start,String end){
        List<Train> trains = dao.getTrains();

        for (Train t : trains){
            t.loadMidStations(dao);
        }


        return  trains.stream().filter(t -> t.isInRoute(dao,start,end)).collect(Collectors.toList());
    }


    void createMockData(TrainDAO dao){
        //Lines
        dao.addLine(new Line(-1,"central"));
        dao.addLine(new Line(-1,"western"));

        //Stations
        dao.addStation(new Station(-1,1,"Dombivli",5,false));
        dao.addStation(new Station(-1,1,"Kopar",5,false));
        dao.addStation(new Station(-1,1,"Diva",5,false));
        dao.addStation(new Station(-1,1,"Mumbra",5,false));
        dao.addStation(new Station(-1,1,"Kalwa",5,false));
        dao.addStation(new Station(-1,1,"Thane",10,true));
        dao.addStation(new Station(-1,2,"Airoli",4,false));

        //Train
        dao.addTrain(new Train(-1,1,7,"2,3,4,5,6",false));

        //Arrivals
        dao.addMidStation(new Arrival(1,1,Timestamp.valueOf("2022-05-02 13:20:23"),2));
        dao.addMidStation(new Arrival(1,2,Timestamp.valueOf("2022-05-02 13:24:23"),2));
        dao.addMidStation(new Arrival(1,3,Timestamp.valueOf("2022-05-02 13:27:23"),4));
        dao.addMidStation(new Arrival(1,4,Timestamp.valueOf("2022-05-02 13:35:23"),4));
        dao.addMidStation(new Arrival(1,5,Timestamp.valueOf("2022-05-02 13:40:23"),4));
        dao.addMidStation(new Arrival(1,6,Timestamp.valueOf("2022-05-02 13:45:23"),4));
        dao.addMidStation(new Arrival(1,7,Timestamp.valueOf("2022-05-02 13:55:23"),2));


    }

    @Test
    public void fetchAvailableTrainsTest(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TrainTimeTableDB db = Room.inMemoryDatabaseBuilder(appContext,TrainTimeTableDB.class)
                .fallbackToDestructiveMigration()
                .build();
        TrainDAO dao = db.getTrainDAO();
        createMockData(dao);


        List<Train> availableTrains = getAvailableTrains(dao,"Dombivli","Airoli");
        assert (availableTrains.size() != 0);

    }
}