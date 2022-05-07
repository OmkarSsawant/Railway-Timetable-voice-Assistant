package com.visionDev.traintimetableassistant.ui.admin.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visionDev.traintimetableassistant.ui.admin.AdminActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class HomeFragment extends Fragment {


    public HomeFragment() { }

    private LineFragment lineFragment;
    private StationFragment stationFragment;
    private TrainFragment trainFragment;
    private TrainDAO trainDAO;

    final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         trainDAO = ((AdminActivity)requireActivity()).db.getTrainDAO();

        lineFragment = LineFragment.newInstance();
        stationFragment = new StationFragment();
                    trainFragment = TrainFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CardView lines = view.findViewById(R.id.lines);
        CardView stations = view.findViewById(R.id.stations);
        CardView trains = view.findViewById(R.id.trains);

        lines.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,lineFragment)
                    .addToBackStack(null)
                    .commit();
        });

        stations.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,stationFragment)
                    .addToBackStack(null)

                    .commit();
        });

        trains.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,trainFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}