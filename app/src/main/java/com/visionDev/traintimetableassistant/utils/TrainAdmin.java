package com.visionDev.traintimetableassistant.utils;

import android.util.Log;

import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TrainAdmin {


   public static  Train getFastTrain(List<Train> trains){
        for (Train t:
             trains) {
            if(t.isFastTrain){
                return  t;
            }
        }
        return  null;
    }

  public   static String getStationName(List<Station> stations,Long stationNo){
        for (Station s:
                stations) {
            if(s.stationNo == stationNo)
                return  s.name;
        }
        return  "Unknown";
    }
    public  static List<Train> getAvailableTrains(List<Train> trains,TrainDAO dao, String start, String end){
        Log.i("DEBUG", "getAvailableTrains: " + trains.size());
        ArrayList<Train> availableTrains = new ArrayList<>();
        List<Station> stations = dao.getStations();
        for (Train t : trains){
            if(t.isInRoute(dao,start,end)){
                availableTrains.add(t);
            }
        }
        //Sort Trains by arrival times
        Collections.sort(availableTrains, (t1, t2) -> {
            Arrival t1a = null,t2a = null;
            for (Arrival a:
                    t1.arrivals) {
                if(getStationName(stations,a.station_id).equalsIgnoreCase(start)){
                    t1a = a;
                    break;
                }
            }
            for (Arrival a:
                    t2.arrivals) {
                if(getStationName(stations,a.station_id).equalsIgnoreCase(start)){
                    t2a = a;
                    break;
                }
            }
            if(t1a == null || t1a.arrivalTime == null || t2a == null  || t2a.arrivalTime == null )
                return  0;

            return Long.compare(t1a.arrivalTime.getTime(), t2a.arrivalTime.getTime());
        });
        return  availableTrains;
    }



  public   static Disposable addTrain(TrainDAO dao, String name, String start, String end, boolean fast){

        Long startId = dao.getIdOfStation(start);
        Long endId = dao.getIdOfStation(end);
        ArrayList<Arrival> arrivals = new ArrayList<>();

        //Train 1
       return   dao.addTrain(new Train(null,name,startId,endId,fast))
                 .observeOn(Schedulers.io())
                 .subscribeOn(AndroidSchedulers.mainThread())
                 .subscribe(trainId -> {
                     Log.i("TAG", "addedTrain: ==================================");
                     //After 30 minutes
                     Timestamp prev = new Timestamp(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(30));

                     if(fast){
                         if (startId < endId) {
                             //first station
                             Timestamp next = new Timestamp(new Date(prev.getTime() + TimeUnit.MINUTES.toMillis(5)).getTime());
                             dao.addArrival(new Arrival(trainId, startId, next, 2)).blockingSubscribe();

                             int midstations = Math.round(endId-startId) ;
                             if(midstations>1){
                                 //middle station
                                 int midStationId = Math.round(startId + Math.round(midstations /2.0));
                                 Timestamp nextm = new Timestamp(new Date(next.getTime() + TimeUnit.MINUTES.toMillis(5)).getTime());
                                 dao.addArrival(new Arrival(trainId, midStationId, nextm, 1)).blockingSubscribe();
                             }

                             //last station
                             Timestamp nextl = new Timestamp(new Date(prev.getTime() + TimeUnit.MINUTES.toMillis(10)).getTime());
                             dao.addArrival(new Arrival(trainId, endId, nextl, 1)).blockingSubscribe();
                         } else {
                             //first station
                             Timestamp next = new Timestamp(new Date(prev.getTime() + TimeUnit.MINUTES.toMillis(5)).getTime());
                             dao.addArrival(new Arrival(trainId, startId, next, 2)).blockingSubscribe();

                             int midstations = Math.round(startId-endId) ;
                             if(midstations>1){
                                 //middle station
                                 int midStationId = Math.round(endId + Math.round(midstations /2.0));
                                 Timestamp nextm = new Timestamp(new Date(next.getTime() + TimeUnit.MINUTES.toMillis(5)).getTime());
                                 dao.addArrival(new Arrival(trainId, midStationId, nextm, 1)).blockingSubscribe();
                             }

                             //last station
                             Timestamp nextl = new Timestamp(new Date(prev.getTime() + TimeUnit.MINUTES.toMillis(10)).getTime());
                             dao.addArrival(new Arrival(trainId, endId, nextl, 1)).blockingSubscribe();
                         }
                     }else {

                         if (startId < endId) {
                             for (long i = startId; i <= endId; i++) {
                                 Timestamp next = new Timestamp(new Date(prev.getTime() + TimeUnit.MINUTES.toMillis(5)).getTime());
                                 dao.addArrival(new Arrival(trainId, i, next, 2)).blockingSubscribe();
                                 prev = next;
                             }

                         } else {
                             for (long i = endId; i >= startId; i--) {
                                 Timestamp next = new Timestamp(new Date(prev.getTime() + TimeUnit.MINUTES.toMillis(5)).getTime());
                                 dao.addArrival(new Arrival(trainId, i, next, 2)).blockingSubscribe();
                                 prev = next;
                             }
                         }
                     }
                 });


    }

    /*
     * ASANGOAN <-> MUMBAI CST
     * VIRAR <-> CHURCHGATE
     * ...
     * New Line can be added the starting station will start from 1
     * as the line no. differs
     * */
  public static void addStations(TrainDAO dao){
        // ===============  CENTRAL LINE =========================
        dao.addStation(new Station(1,1,"Asangoan",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(2,1,"Vasind",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(3,1,"Khadavli",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(4,1,"Titwala",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(5,1,"Ambivli",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(6,1,"Shahad",5,false)).blockingSubscribe(e->{});

//        dao.addStation(new Station(-1,1,"Karjat",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Bhivpuri",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Neral",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Sheru",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Wangani",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Badlapur",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Ambernath",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Ulhasnagar",5,false)).blockingSubscribe(e->{});
//        dao.addStation(new Station(-1,1,"Vitthalwadi",5,false)).blockingSubscribe(e->{});
//


        dao.addStation(new Station(7,1,"Kalyan",5,true)).blockingSubscribe();

        dao.addStation(new Station(8,1,"Thakurli",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(9,1,"Dombivli",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(10,1,"Kopar",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(11,1,"Diva",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(12,1,"Mumbra",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(13,1,"Kalwa",5,false)).blockingSubscribe(e->{});

        dao.addStation(new Station(14,1,"Thane",10,true)).blockingSubscribe();

        dao.addStation(new Station(15,1,"Mulund",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(16,1,"Nahur",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(17,1,"Bhandup",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(18,1,"Kanjurmarg",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(19,1,"Vikhroli",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(20,1,"ghatkopar",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(21,1,"Vidyavihar",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(22,1,"Kurla",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(23,1,"Sion",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(24,1,"Matunga",5,false)).blockingSubscribe(e->{});

        dao.addStation(new Station(25,1,"Dadar",10,true)).blockingSubscribe(e->{});



        dao.addStation(new Station(26,1,"Parel",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(27,1,"Currey Road",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(28,1,"Chinch Pokali",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(29,1,"Bykulla",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(31,1,"Sandhurst",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(32,1,"Masjit",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(33,1,"Mumbai CST",5,false)).blockingSubscribe(e->{});



        // ===============  WESTERN LINE =========================


        dao.addStation(new Station(101,2,"Virar",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(102,2,"Nalasopara",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(103,2,"Vasai",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(104,2,"Naigoan",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(105,2,"Bhayander",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(106,2,"Mira Rd.",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(107,2,"Dahisar",5,false)).blockingSubscribe(e->{});

        dao.addStation(new Station(108,2,"Borivili",10,true)).blockingSubscribe();

        dao.addStation(new Station(109,2,"Kandivli",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(110,2,"Malad",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(111,2,"Goregoan",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(112,2,"Jogeshwari",5,false)).blockingSubscribe(e->{});

        dao.addStation(new Station(113,2,"Andheri",5,true)).blockingSubscribe();
        dao.addStation(new Station(114,2,"Vile Parle",10,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(115,2,"Santacruz",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(116,2,"Khar rd.",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(117,2,"Bandra",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(118,2,"Mahim",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(119,2,"Matunga",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(120,2,"Dadar",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(121,2,"Elphison",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(122,2,"Lower Parel",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(123,2,"Mahalaxmi",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(124,2,"Mumbai Central",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(125,2,"Grant Rd.",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(126,2,"Charni rd.",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(127,2,"Marine Lines",5,false)).blockingSubscribe(e->{});
        dao.addStation(new Station(128,2,"Church Gate",5,false)).blockingSubscribe(e->{});

    }

//
//    void addTrains(TrainDAO dao){
//
//        //Train 1
//        dao.addTrain(new Train(null,"asangoan-thane",1,14,false))
//                .blockingSubscribe(trainId -> {
//                    //Train 1 Arrivals
//                    dao.addArrival(new Arrival(trainId,1, Timestamp.valueOf("2022-05-02 13:20:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,2,Timestamp.valueOf("2022-05-02 13:24:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,3,Timestamp.valueOf("2022-05-02 13:27:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,4,Timestamp.valueOf("2022-05-02 13:45:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,5,Timestamp.valueOf("2022-05-02 13:50:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,6,Timestamp.valueOf("2022-05-02 13:55:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,7,Timestamp.valueOf("2022-05-02 13:59:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,8,Timestamp.valueOf("2022-05-02 14:00:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,9,Timestamp.valueOf("2022-05-02 14:05:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,10,Timestamp.valueOf("2022-05-02 14:10:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,11,Timestamp.valueOf("2022-05-02 14:15:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,12,Timestamp.valueOf("2022-05-02 14:25:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,13,Timestamp.valueOf("2022-05-02 14:30:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId,14,Timestamp.valueOf("2022-05-02 14:40:23"),2)).blockingSubscribe(e->{});
//
//                });
//
//
//
//        //Train 2
//        dao.addTrain(new Train(null,"dadar-andheri",25,13,false))
//                .blockingSubscribe(trainId1->{
//                    dao.addArrival(new Arrival(trainId1,25,Timestamp.valueOf("2022-05-02 13:20:23"),1));
//                    dao.addArrival(new Arrival(trainId1,113,Timestamp.valueOf("2022-05-02 13:24:23"),2)).blockingSubscribe(e->{});
//                });
//
//
//
//        dao.addTrain(new Train(null,"thane-asangoan",14,1,false))
//                .blockingSubscribe(trainId3->{
//                    dao.addArrival(new Arrival(trainId3,14,Timestamp.valueOf("2022-05-02 15:20:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,13,Timestamp.valueOf("2022-05-02 15:24:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,12,Timestamp.valueOf("2022-05-02 15:27:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,11,Timestamp.valueOf("2022-05-02 15:45:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,10,Timestamp.valueOf("2022-05-02 15:50:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,9,Timestamp.valueOf("2022-05-02 15:55:23"),4)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,8,Timestamp.valueOf("2022-05-02 15:59:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,7,Timestamp.valueOf("2022-05-02 16:00:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,6,Timestamp.valueOf("2022-05-02 16:05:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,5,Timestamp.valueOf("2022-05-02 16:10:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,4,Timestamp.valueOf("2022-05-02 16:15:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,3,Timestamp.valueOf("2022-05-02 16:25:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,2,Timestamp.valueOf("2022-05-02 16:30:23"),2)).blockingSubscribe(e->{});
//                    dao.addArrival(new Arrival(trainId3,1,Timestamp.valueOf("2022-05-02 16:40:23"),2)).blockingSubscribe(e->{});
//
//                });
//
//
//    }
//

    public static void addLines(TrainDAO dao){
        //Lines
        dao.addLine(new Line(1,"central")).blockingSubscribe();
        dao.addLine(new Line(2,"western")).blockingSubscribe();
    }



    public interface AvailableTrainCallback{
       void onAvailableTrains(List<Train> trains);
    }

   public static Disposable getAvailableTrains(TrainDAO dao, String start, String end , AvailableTrainCallback trainCallback){
       return dao.getTrains()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ts -> {
                    Log.i("TAG", "onCreate: " + ts.size());
                    final ArrayList<Train> trains = new ArrayList<>(ts);
                    final ArrayList<Observable<List<Arrival>>> tasks = new ArrayList<>();

                    for (Train t:trains){
                        tasks.add(t.loadArrivals(dao));
                    }

                    Observable.fromArray(tasks)
                            .subscribeOn(Schedulers.io())
                            .blockingSubscribe(tks -> {

                                Log.i("doOnComplete", "onCreate: GOT ARRIVALS" );

                                for (int i = 0; i < trains.size(); i++) {
                                    trains.get(i).arrivals = tks.get(i).blockingFirst();
                                    Log.i("ARRIVAL", "onCreate: [" + i + "] " +trains.get(i).arrivals.size());
                                }

                                List<Train> availableTrains = TrainAdmin.getAvailableTrains(trains,dao,start,end);
                                trainCallback.onAvailableTrains(availableTrains);
                            });
                },err->{});
    }
}
