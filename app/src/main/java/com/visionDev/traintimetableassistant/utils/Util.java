package com.visionDev.traintimetableassistant.utils;

import android.util.Log;

import com.visionDev.traintimetableassistant.data.doa.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public  static List<Train> getAvailableTrains(List<Train> trains,TrainDAO dao, String start, String end){
        Log.i("DEBUG", "getAvailableTrains: " + trains.size());
        ArrayList<Train> availableTrains = new ArrayList<>();
        for (Train t : trains){
            if(t.isInRoute(dao,start,end)){
                availableTrains.add(t);
            }
        }

        return  availableTrains;
    }


    static
        /*
         * ASANGOAN <-> MUMBAI CST
         * VIRAR <-> CHURCHGATE
         * ...
         * New Line can be added the starting station will start from 1
         * as the line no. differs
         * */
    void addWesternAndCentralStations(TrainDAO dao){
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


    static List<Arrival> addTrain(TrainDAO dao,String name,long startId,long endId){

        ArrayList<Arrival> arrivals = new ArrayList<>();

        //Train 1
        dao.addTrain(new Train(null,name,startId,endId,false))
                .doOnSuccess(trainId -> {
                    for(long i = startId;i <=endId;i++){
                        /* TODO: TimeStamp inverval maker and */

                        dao.addArrival(new Arrival(trainId,i,Timestamp.valueOf("2022-05-02 13:24:23"),2));
                    }
                });


        return  arrivals;
    }


}
