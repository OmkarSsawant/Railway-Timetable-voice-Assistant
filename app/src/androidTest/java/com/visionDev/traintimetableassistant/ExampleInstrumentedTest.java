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
import com.visionDev.traintimetableassistant.utils.Util;

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
            t.loadArrivals(dao);
        }


        return  trains.stream().filter(t -> t.isInRoute(dao,start,end)).collect(Collectors.toList());
    }



    /*
    * ASANGOAN <-> MUMBAI CST
    * VIRAR <-> CHURCHGATE
    * ...
    * New Line can be added the starting station will start from 1
    * as the line no. differs
    * */
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



        // ===============  WESTERN LINE =========================


        dao.addStation(new Station(101,2,"Virar",5,false));
        dao.addStation(new Station(102,2,"Nalasopara",5,false));
        dao.addStation(new Station(103,2,"Vasai",5,false));
        dao.addStation(new Station(104,2,"Naigoan",5,false));
        dao.addStation(new Station(105,2,"Bhayander",5,false));
        dao.addStation(new Station(106,2,"Mira Rd.",5,false));
        dao.addStation(new Station(107,2,"Dahisar",5,false));

        dao.addStation(new Station(108,2,"Borivili",10,true));

        dao.addStation(new Station(109,2,"Kandivli",5,false));
        dao.addStation(new Station(110,2,"Malad",5,false));
        dao.addStation(new Station(111,2,"Goregoan",5,false));
        dao.addStation(new Station(112,2,"Jogeshwari",5,false));

        dao.addStation(new Station(113,2,"Andheri",5,true));

        dao.addStation(new Station(114,2,"Vile Parle",10,false));
        dao.addStation(new Station(115,2,"Santacruz",5,false));
        dao.addStation(new Station(116,2,"Khar rd.",5,false));
        dao.addStation(new Station(117,2,"Bandra",5,false));
        dao.addStation(new Station(118,2,"Mahim",5,false));
        dao.addStation(new Station(119,2,"Matunga",5,false));
        dao.addStation(new Station(120,2,"Dadar",5,false));
        dao.addStation(new Station(121,2,"Elphison",5,false));
        dao.addStation(new Station(122,2,"Lower Parel",5,false));
        dao.addStation(new Station(123,2,"Mahalaxmi",5,false));
        dao.addStation(new Station(124,2,"Mumbai Central",5,false));
        dao.addStation(new Station(125,2,"Grant Rd.",5,false));
        dao.addStation(new Station(126,2,"Charni rd.",5,false));
        dao.addStation(new Station(127,2,"Marine Lines",5,false));
        dao.addStation(new Station(128,2,"Church Gate",5,false));

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


        //Train 2
        long trainId1 = dao.addTrain(new Train(-1,"dadar-andheri",25,13,false));

        //Train 1 Arrivals
        dao.addArrival(new Arrival(trainId1,25,Timestamp.valueOf("2022-05-02 13:20:23"),1));
        dao.addArrival(new Arrival(trainId1,113,Timestamp.valueOf("2022-05-02 13:24:23"),2));


        long trainId3 = dao.addTrain(new Train(-1,"kalyan-thane",1,14,false));

        //Train 1 Arrivals
        dao.addArrival(new Arrival(trainId3,14,Timestamp.valueOf("2022-05-02 15:20:23"),2));
        dao.addArrival(new Arrival(trainId3,13,Timestamp.valueOf("2022-05-02 15:24:23"),2));
        dao.addArrival(new Arrival(trainId3,12,Timestamp.valueOf("2022-05-02 15:27:23"),4));
        dao.addArrival(new Arrival(trainId3,11,Timestamp.valueOf("2022-05-02 15:45:23"),4));
        dao.addArrival(new Arrival(trainId3,10,Timestamp.valueOf("2022-05-02 15:50:23"),4));
        dao.addArrival(new Arrival(trainId3,9,Timestamp.valueOf("2022-05-02 15:55:23"),4));
        dao.addArrival(new Arrival(trainId3,8,Timestamp.valueOf("2022-05-02 15:59:23"),2));
        dao.addArrival(new Arrival(trainId3,7,Timestamp.valueOf("2022-05-02 16:00:23"),2));
        dao.addArrival(new Arrival(trainId3,6,Timestamp.valueOf("2022-05-02 16:05:23"),2));
        dao.addArrival(new Arrival(trainId3,5,Timestamp.valueOf("2022-05-02 16:10:23"),2));
        dao.addArrival(new Arrival(trainId3,4,Timestamp.valueOf("2022-05-02 16:15:23"),2));
        dao.addArrival(new Arrival(trainId3,3,Timestamp.valueOf("2022-05-02 16:25:23"),2));
        dao.addArrival(new Arrival(trainId3,2,Timestamp.valueOf("2022-05-02 16:30:23"),2));
        dao.addArrival(new Arrival(trainId3,1,Timestamp.valueOf("2022-05-02 16:40:23"),2));


    }



    void createMockData(TrainDAO dao){
        //Lines
        dao.addLine(new Line(1,"central"));
        dao.addLine(new Line(2,"western"));

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




        List<Train> availableTrains1 = Util.getAvailableTrains(dao,"Titwala","Mumbai CST");
        for (Train t:
             availableTrains1) {
            Log.i("RESULT",t.toString());
        }
        assert (availableTrains1.size() != 0);

        List<Train> availableTrains2 = Util.getAvailableTrains(dao,"Mumbai CST","Titwala");

        assert (availableTrains2.size() != 0);


    }
}