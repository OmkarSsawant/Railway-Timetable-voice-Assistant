package com.visionDev.traintimetableassistant.ui.admin.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.visionDev.traintimetableassistant.ui.admin.AdminActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;
import com.visionDev.traintimetableassistant.ui.admin.adapters.ArrivalRVAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AddTrainFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    public AddTrainFragment() {

    }
    final ArrayList<String> stationNames=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       stationNames.clear();
       stationNames.addAll(((AdminActivity) requireActivity()).db.getTrainDAO().getStationNames());
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_train, container, false);
    }

    Timestamp curArrivalTime;
    void showAlertDialog(LayoutInflater lf, Activity activity, Long trainId,TrainDAO dao){
        final AlertDialog.Builder b = new AlertDialog.Builder(activity).setCancelable(true);
        View v = lf.inflate(R.layout.dialog_add_mid_station,null,false);
        b.setView(v);

        final ImageButton dateTimePicker = v.findViewById(R.id.arrival_time);
        final Spinner ms = v.findViewById(R.id.mid_stations);
        final EditText pfn = v.findViewById(R.id.pfn);

        ArrayAdapter<String> station = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,stationNames);
        ms.setAdapter(station);
        ms.setSelection(0);
        dateTimePicker.setOnClickListener(q->{
            final Calendar c = Calendar.getInstance();
            DatePickerDialog d = new DatePickerDialog(activity,this,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
            d.show();
        });
        b.setPositiveButton(R.string.submit,(d,z)->{
                Long stationId = dao.getIdOfStation((String) ms.getSelectedItem());
                adapter.addArrivals(new Arrival(trainId,stationId,curArrivalTime,Integer.parseInt(pfn.getText().toString())));
            d.dismiss();
        });
        b.show();
    }
    ArrivalRVAdapter adapter;
    Train train;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton  dmt = view.findViewById(R.id.add_mid);
        Spinner start = view.findViewById(R.id.start);
        Spinner end = view.findViewById(R.id.end);
        EditText tname = view.findViewById(R.id.train_name);
        CheckBox isFastC = view.findViewById(R.id.is_fast);
        RecyclerView arrivals = view.findViewById(R.id.middle_stations_list);
         adapter = new ArrivalRVAdapter(((AdminActivity) requireActivity()).db.getTrainDAO());
        arrivals.setLayoutManager(new LinearLayoutManager(getContext()));
        arrivals.setAdapter(adapter);


        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,stationNames);
        start.setAdapter(startAdapter);
        start.setSelection(0);
        ArrayAdapter<String> endAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,stationNames);
        end.setAdapter(endAdapter);
        end.setSelection(4);

        TrainDAO dao = ((AdminActivity)requireActivity()).db.getTrainDAO();
        final CompositeDisposable cd  = new CompositeDisposable();
        dmt.setOnClickListener(v ->
        {
            //insert train
            Long startStationId = dao.getIdOfStation((String) start.getSelectedItem() );
            Long endStationId = dao.getIdOfStation((String) end.getSelectedItem() );;
            String name = tname.getText().toString();
            boolean isFast  = isFastC.isChecked() ;
            train = new Train(null,name,startStationId,endStationId,isFast);
             cd.add( dao.addTrain(train)
             .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(id -> {
                train.id = id;
                showAlertDialog(getLayoutInflater(),requireActivity(),id,dao);
            }))
            ;

        });

        Button done = view.findViewById(R.id.done);
        done.setOnClickListener(v->{

            cd.add(
            dao.updateTrain(train)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(urc->{
                        cd.clear();
                        Toast.makeText(v.getContext(),"Successfully Added Train ",Toast.LENGTH_SHORT).show();
                    })
            );

            cd.add(
                    dao.addArrivals(adapter.getArrivals())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(no->{
                        Log.i("TAG", "onViewCreated: Added Arrivals");
                    })
            );

        });
    }
    int year,month,day;

    @Override
    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
        final Calendar c = Calendar.getInstance();
        year = y;
        month = m;
        day = d;
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),this,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
        tpd.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int h, int m) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,h);
        c.set(Calendar.MINUTE,m);
        c.set(Calendar.DAY_OF_MONTH,day);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);
        curArrivalTime = new Timestamp(c.getTimeInMillis());
        Log.i("TIME", "onTimeSet: " + h + " : "+m + " +> " + day + "/"+month + "/"+year);
    }
}