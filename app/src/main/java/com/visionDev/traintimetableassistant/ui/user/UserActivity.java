package com.visionDev.traintimetableassistant.ui.user;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.visionDev.traintimetableassistant.MainActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;
import com.visionDev.traintimetableassistant.data.room.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.ui.admin.HomeFragment;
import com.visionDev.traintimetableassistant.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {


    Pair<Train, Arrival> getFirstTrain(List<Train> trainList, List<Station> stations, String start){
        if(trainList.isEmpty()) return  null;
        Train ft = trainList.get(0);
        return  withArrival(ft,stations,start);
    }


    int trainUsedIndex = 1;
    Pair<Train, Arrival> getNextTrain(List<Train> trainList, List<Station> stations, String start){
        if(trainUsedIndex >= trainList.size() || trainList.isEmpty()) return  null;
        Train ft = trainList.get(trainUsedIndex);
        trainUsedIndex++;
        return  withArrival(ft,stations,start);
    }

    Pair<Train, Arrival> getFastTrain(List<Train> trainList, List<Station> stations, String start){
        if(trainList.isEmpty()) return  null;
        Train ft = Util.getFastTrain(trainList);
        if(ft==null) return  null;
        return  withArrival(ft,stations,start);
    }


    Pair<Train, Arrival> withArrival(Train t,List<Station> stations, String start){
        Arrival ta = null;
        for (Arrival a:
                t.arrivals) {
            if(Util.getStationName(stations,a.station_id).equals(start)){
                ta = a;
                break;
            }
        }
        return  new Pair<>(t,ta);
    }

    String createMessage(Pair<Train,Arrival> res){
        if (res==null) return  "No Train Available currently";
        return  "The Train "+ res.first.name +" will be arriving on " + res.second.arrivalTime.toString() + " on platform number " + res.second.platformNumber + (res.first.isFastTrain ? " This is a fast train" : "This is a slow train");
    }

    public TrainTimeTableDB db;
    private  Spinner start,end;
    private TextView latest,fast,next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        db = Room.databaseBuilder(this,TrainTimeTableDB.class,"train_system")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        TrainDAO dao = db.getTrainDAO();
        Util.addLines(dao);
        Util.addStations(dao);
        Util.addTrain(dao,"titwala-thane","Titwala","Mumbai CST",false);
        Util.addTrain(dao,"thane-titwala","Mumbai CST","Titwala",false);
        Util.addTrain(dao,"dombivli-thane","Dombivli","Thane",false);
        Util.addTrain(dao,"thane-dombivli","Thane","Dombivli",false);

        final ArrayList<String> stationNames=new ArrayList<>(dao.getStationNames());
        final List<Station> stations = dao.getStations().blockingGet();
        start = findViewById(R.id.start_src);
         end = findViewById(R.id.end_dest);
         ArrayAdapter<String> a1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,stationNames);
        ArrayAdapter<String> a2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,stationNames);
        latest = findViewById(R.id.latest_train);
        fast = findViewById(R.id.fast_train);
        next = findViewById(R.id.next_train);
        start.setAdapter(a1);
        end.setAdapter(a2);
        Button search = findViewById(R.id.get_trains);
        search.setOnClickListener(view -> {
            String s = (String) start.getSelectedItem();
            String e = (String) end.getSelectedItem();
             Util.getAvailableTrains(dao, s, e, availableTrains -> {
                 latest.setText(createMessage(getFirstTrain(availableTrains,stations,s)));
                 fast.setText(createMessage(getFastTrain(availableTrains,stations,s)));
                 next.setText(createMessage(getNextTrain(availableTrains,stations,s)));
             });

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
