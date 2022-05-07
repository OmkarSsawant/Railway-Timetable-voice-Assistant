package com.visionDev.traintimetableassistant.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.room.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.ui.admin.screens.HomeFragment;
import com.visionDev.traintimetableassistant.utils.TrainAdmin;

public class AdminActivity extends AppCompatActivity {


   public TrainTimeTableDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         db = Room.databaseBuilder(this,TrainTimeTableDB.class,"train_system")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

         TrainAdmin.addLines(db.getTrainDAO());
         TrainAdmin.addStations(db.getTrainDAO());



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