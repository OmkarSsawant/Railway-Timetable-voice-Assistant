package com.visionDev.traintimetableassistant.ui.admin.screens;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visionDev.traintimetableassistant.ui.admin.AdminActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.ui.admin.adapters.TrainRecyclerViewAdapter;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class TrainFragment extends Fragment {


    public TrainFragment() {
    }


    public static TrainFragment newInstance() {
        return new TrainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_train_list, container, false);
    }

    TrainRecyclerViewAdapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
         adapter = new TrainRecyclerViewAdapter(new ArrayList<>(),((AdminActivity)requireActivity()).db.getTrainDAO());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        FloatingActionButton fb = view.findViewById(R.id.add_train);

        fb.setOnClickListener(w->{
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,new AddTrainFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }



    Disposable ds;

    @Override
    public void onResume() {
        super.onResume();
        ds  = ((AdminActivity)requireActivity()).db.getTrainDAO()
                .observeTrains()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(ts -> {
                    Log.i("TAG", "onResume: "+ts.size());
                    adapter.setTrains(ts);
                });


    }

    @Override
    public void onPause() {
        super.onPause();
        ds.dispose();
    }
}