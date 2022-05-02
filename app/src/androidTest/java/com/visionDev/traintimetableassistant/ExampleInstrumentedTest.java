package com.visionDev.traintimetableassistant;

import android.content.Context;
import android.util.Log;

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


    void addStations(TrainDAO dao){
        // ===============  CENTRAL LINE =========================
        dao.addStation(new Station(1,1,"Asangoan",5,false));
        dao.addStation(new Station(2,1,"Vasind",5,false));
        dao.addStation(new Station(3,1,"Khadavli",5,false));
        dao.addStation(new Station(4,1,"Titwala",5,false));
        dao.addStation(new Station(5,1,"Ambivli",5,false));
        dao.addStation(new Station(6,1,"Shahad",5,false));

//        dao.addStation(new Station(-1,1,"Karjat",5,false));
//        dao.addStation(new Station(-1,1,"Bhivpuri",5,false));
//        dao.addStation(new Station(-1,1,"Neral",5,false));
//        dao.addStation(new Station(-1,1,"Sheru",5,false));
//        dao.addStation(new Station(-1,1,"Wangani",5,false));
//        dao.addStation(new Station(-1,1,"Badlapur",5,false));
//        dao.addStation(new Station(-1,1,"Ambernath",5,false));
//        dao.addStation(new Station(-1,1,"Ulhasnagar",5,false));
//        dao.addStation(new Station(-1,1,"Vitthalwadi",5,false));
//


        dao.addStation(new Station(7,1,"Kalyan",5,true));

        dao.addStation(new Station(8,1,"Thakurli",5,false));
        dao.addStation(new Station(9,1,"Dombivli",5,false));
        dao.addStation(new Station(10,1,"Kopar",5,false));
        dao.addStation(new Station(11,1,"Diva",5,false));
        dao.addStation(new Station(12,1,"Mumbra",5,false));
        dao.addStation(new Station(13,1,"Kalwa",5,false));

        dao.addStation(new Station(14,1,"Thane",10,true));

        dao.addStation(new Station(15,1,"Mulund",5,false));
        dao.addStation(new Station(16,1,"Nahur",5,false));
        dao.addStation(new Station(17,1,"Bhandup",5,false));
        dao.addStation(new Station(18,1,"Kanjurmarg",5,false));
        dao.addStation(new Station(19,1,"Vikhroli",5,false));
        dao.addStation(new Station(20,1,"ghatkopar",5,false));
        dao.addStation(new Station(21,1,"Vidyavihar",5,false));
        dao.addStation(new Station(22,1,"Kurla",5,false));
        dao.addStation(new Station(23,1,"Sion",5,false));
        dao.addStation(new Station(24,1,"Matunga",5,false));

        dao.addStation(new Station(25,1,"Dadar",10,true));

        dao.addStation(new Station(26,1,"Parel",5,false));
        dao.addStation(new Station(27,1,"Currey Road",5,false));
        dao.addStation(new Station(28,1,"Chinch Pokali",5,false));
        dao.addStation(new Station(29,1,"Bykulla",5,false));
        dao.addStation(new Station(31,1,"Sandhurst",5,false));
        dao.addStation(new Station(32,1,"Masjit",5,false));
        dao.addStation(new Station(33,1,"Mumbai CST",5,false));

//        dao.addStation(new Station(15,2,"Airoli",4,false));
//        dao.addStation(new Station(16,2,"Rable",4,false));
//        dao.addStation(new Station(17,2,"Ghansoli",4,false));
//        dao.addStation(new Station(18,2,"Koparkhairane",4,false));
//        dao.addStation(new Station(19,2,"Turbhe",4,false));


        // ===============  WESTERN LINE =========================


        dao.addStation(new Station(1,2,"Virar",5,false));
        dao.addStation(new Station(2,2,"Nalasopara",5,false));
        dao.addStation(new Station(3,2,"Vasai",5,false));
        dao.addStation(new Station(4,2,"Naigoan",5,false));
        dao.addStation(new Station(5,2,"Bhayander",5,false));
        dao.addStation(new Station(6,2,"Mira Rd.",5,false));
        dao.addStation(new Station(7,2,"Dahisar",5,false));

        dao.addStation(new Station(8,2,"Borivili",10,true));

        dao.addStation(new Station(9,2,"Kandivli",5,false));
        dao.addStation(new Station(10,2,"Malad",5,false));
        dao.addStation(new Station(11,2,"Goregoan",5,false));
        dao.addStation(new Station(12,2,"Jogeshwari",5,false));

        dao.addStation(new Station(13,2,"Andheri",5,true));

        dao.addStation(new Station(14,2,"Vile Parle",10,false));
        dao.addStation(new Station(15,2,"Santacruz",5,false));
        dao.addStation(new Station(16,2,"Khar rd.",5,false));
        dao.addStation(new Station(17,2,"Bandra",5,false));
        dao.addStation(new Station(18,2,"Mahim",5,false));
        dao.addStation(new Station(19,2,"Matunga",5,false));
        dao.addStation(new Station(20,2,"Dadar",5,false));
        dao.addStation(new Station(21,2,"Elphison",5,false));
        dao.addStation(new Station(22,2,"Lower Parel",5,false));
        dao.addStation(new Station(23,2,"Mahalaxmi",5,false));
        dao.addStation(new Station(24,2,"Mumbai Central",5,false));
        dao.addStation(new Station(25,2,"Grant Rd.",5,false));
        dao.addStation(new Station(26,2,"Charni rd.",5,false));
        dao.addStation(new Station(27,2,"Marine Lines",5,false));
        dao.addStation(new Station(28,2,"Church Gate",5,false));

    }


    void addTrains(TrainDAO dao){

        //Train 1
        long trainId = dao.addTrain(new Train(-1,"kalyan-thane",1,14,false));

        //Train 1 Arrivals
        dao.addArrival(new Arrival(trainId,1,Timestamp.valueOf("2022-05-02 13:20:23"),2));
        dao.addArrival(new Arrival(trainId,2,Timestamp.valueOf("2022-05-02 13:24:23"),2));
        dao.addArrival(new Arrival(trainId,3,Timestamp.valueOf("2022-05-02 13:27:23"),4));
        dao.addArrival(new Arrival(trainId,4,Timestamp.valueOf("2022-05-02 13:45:23"),4));
        dao.addArrival(new Arrival(trainId,5,Timestamp.valueOf("2022-05-02 13:50:23"),4));
        dao.addArrival(new Arrival(trainId,6,Timestamp.valueOf("2022-05-02 13:55:23"),4));
        dao.addArrival(new Arrival(trainId,7,Timestamp.valueOf("2022-05-02 13:59:23"),2));
        dao.addArrival(new Arrival(trainId,8,Timestamp.valueOf("2022-05-02 14:00:23"),2));
        dao.addArrival(new Arrival(trainId,9,Timestamp.valueOf("2022-05-02 14:05:23"),2));
        dao.addArrival(new Arrival(trainId,10,Timestamp.valueOf("2022-05-02 14:10:23"),2));
        dao.addArrival(new Arrival(trainId,11,Timestamp.valueOf("2022-05-02 14:15:23"),2));
        dao.addArrival(new Arrival(trainId,12,Timestamp.valueOf("2022-05-02 14:25:23"),2));
        dao.addArrival(new Arrival(trainId,13,Timestamp.valueOf("2022-05-02 14:30:23"),2));
        dao.addArrival(new Arrival(trainId,14,Timestamp.valueOf("2022-05-02 14:40:23"),2));

    }
    void createMockData(TrainDAO dao){
        //Lines
        dao.addLine(new Line(-1,"central"));
        dao.addLine(new Line(-1,"western"));

        addStations(dao);
        addTrains(dao);
    }

    @Test
    public void fetchAvailableTrainsTest(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TrainTimeTableDB db = Room.inMemoryDatabaseBuilder(appContext,TrainTimeTableDB.class)
                .fallbackToDestructiveMigration()
                .build();
        TrainDAO dao = db.getTrainDAO();
        createMockData(dao);


        List<Train> availableTrains = getAvailableTrains(dao,"Dombivli","Thane");
        for (Train t:
             availableTrains) {
            Log.i("RESULT",t.toString());
        }
        assert (availableTrains.size() != 0);

    }
}