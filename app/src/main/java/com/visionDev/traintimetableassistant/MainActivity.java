package com.visionDev.traintimetableassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.visionDev.traintimetableassistant.data.room.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.utils.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    TrainTimeTableDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         db = Room.databaseBuilder(this,TrainTimeTableDB.class,"train_system")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}