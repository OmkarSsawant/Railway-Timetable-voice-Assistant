package com.visionDev.traintimetableassistant.ui.admin.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visionDev.traintimetableassistant.ui.admin.AdminActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.ui.admin.adapters.StationRecyclerViewAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class StationFragment extends Fragment {


    private ArrayList<Station> mStations = new ArrayList<>();
    final CompositeDisposable cd  =new CompositeDisposable();

    public StationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_station_list, container, false);
    }


   public static void showAlertDialog(Station s, LayoutInflater lf, Activity activity,StationRecyclerViewAdapter adapter){
        final AlertDialog.Builder b = new AlertDialog.Builder(activity).setCancelable(true);
        View v = lf.inflate(R.layout.dialog_add_station,null,false);
        b.setView(v);
        EditText lnet = v.findViewById(R.id.station_name_e);
        EditText stn = v.findViewById(R.id.station_no_e);
        EditText lnoet = v.findViewById(R.id.line_number_e);
        EditText pc = v.findViewById(R.id.platform_count);
        CheckBox mp = v.findViewById(R.id.isMP);
        if(s!=null){
            lnet.setText(s.name);
            stn.setText(s.stationNo+"");
            lnoet.setText(s.lineId+"");
            pc.setText(s.noOfPlatforms+"");
            mp.setChecked(s.isMajorStation);
        }


        b.setPositiveButton(R.string.submit,(d,z)->{
            String name =  lnet.getText().toString();
            int lno = Integer.parseInt( lnoet.getText().toString());
            int sno = Integer.parseInt( stn.getText().toString());
            int plc = Integer.parseInt( pc.getText().toString());
            Station n = new Station(sno,lno,name,plc,mp.isChecked());
            ((AdminActivity) activity).db.getTrainDAO()
                    .addStation(n)
                    .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe( k->{});
            d.dismiss();
        });
        b.show();
    }
    StationRecyclerViewAdapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
         adapter = new StationRecyclerViewAdapter(mStations,(AdminActivity) requireActivity());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        FloatingActionButton fb = view.findViewById(R.id.add_station);

        fb.setOnClickListener(w-> showAlertDialog(null,getLayoutInflater(),requireActivity(),adapter));
    }


    @Override
    public void onResume() {
        super.onResume();
        cd.add(
         ((AdminActivity) requireActivity()).db.getTrainDAO().observeStations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers
                .io())
                .subscribe(s -> {
                    adapter.setStations(s);
                })
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        cd.clear();
    }
}