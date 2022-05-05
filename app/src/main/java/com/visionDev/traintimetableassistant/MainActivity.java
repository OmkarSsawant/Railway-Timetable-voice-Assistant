package com.visionDev.traintimetableassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.visionDev.traintimetableassistant.data.room.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.ui.admin.HomeFragment;
import com.visionDev.traintimetableassistant.utils.Util;

public class MainActivity extends AppCompatActivity {


   public TrainTimeTableDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         db = Room.databaseBuilder(this,TrainTimeTableDB.class,"train_system")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

         Util.addLines(db.getTrainDAO());
         Util.addStations(db.getTrainDAO());



        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.host,new HomeFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}