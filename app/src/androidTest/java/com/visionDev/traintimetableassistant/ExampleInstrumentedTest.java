package com.visionDev.traintimetableassistant;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.visionDev.traintimetableassistant.data.room.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.utils.Util;

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


    void createMockData(TrainDAO dao){
        Util.addLines(dao);
        Util.addStations(dao);
        Util.addTrain(dao,"titwala-thane","Titwala","Mumbai CST",false);
        Util.addTrain(dao,"thane-titwala","Mumbai CST","Titwala",false);
        Util.addTrain(dao,"dombivli-thane","Dombivli","Thane",false);
        Util.addTrain(dao,"thane-dombivli","Thane","Dombivli",false);

    }

    @Test
    public void fetchAvailableTrainsTest(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TrainTimeTableDB db = Room.inMemoryDatabaseBuilder(appContext,TrainTimeTableDB.class)
                .fallbackToDestructiveMigration()
                .build();
        TrainDAO dao = db.getTrainDAO();
        createMockData(dao);




        List<Train> availableTrains1 = Util.getAvailableTrains(dao.getTrains().blockingGet(),dao,"Titwala","Mumbai CST");
        for (Train t:
             availableTrains1) {
            Log.i("RESULT1",t.toString());
        }
        assert (availableTrains1.size() != 0);

        //First Train
        Train ft = availableTrains1.get(0);

        //Next Train
        Train st = availableTrains1.get(1);

        //Fast Train
        Train ftt = Util.getFastTrain(availableTrains1);




        List<Train> availableTrains2 = Util.getAvailableTrains(dao.getTrains().blockingGet(),dao,"Mumbai CST","Titwala");
        for (Train t:
                availableTrains2) {
            Log.i("RESULT2",t.toString());
        }
        assert (availableTrains2.size() != 0);


    }
}