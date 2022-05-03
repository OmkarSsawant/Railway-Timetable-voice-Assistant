package com.visionDev.traintimetableassistant.ui.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.ui.admin.adapters.LineRecyclerViewAdapter;
import com.visionDev.traintimetableassistant.ui.admin.adapters.StationRecyclerViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;


public class StationFragment extends Fragment {


    private static final String ARG_STATIONS = "train.stations";
    private ArrayList<Station> mStations;

    public StationFragment() {
    }

    public static StationFragment newInstance(ArrayList<Station> stations) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STATIONS, stations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable arg =  requireArguments().getSerializable(ARG_STATIONS);
        if(arg instanceof ArrayList){
            mStations = (ArrayList<Station>) arg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_station_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        StationRecyclerViewAdapter adapter = new StationRecyclerViewAdapter(mStations);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }
}