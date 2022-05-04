package com.visionDev.traintimetableassistant.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visionDev.traintimetableassistant.MainActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    public HomeFragment() { }

    private LineFragment lineFragment;
    private StationFragment stationFragment;
    private TrainFragment trainFragment;
    private TrainDAO trainDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         trainDAO = ((MainActivity)requireActivity()).db.getTrainDAO();

        lineFragment = LineFragment.newInstance();
        stationFragment = new StationFragment();
        trainFragment = TrainFragment.newInstance(new ArrayList<>(trainDAO.getTrains().blockingGet()));
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
            List<Line> l = trainDAO.getLines().blockingGet();
            Log.i("TAG", "onViewCreated: " + l.size());

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,lineFragment)
                    .addToBackStack(null)
                    .commit();
        });

        stations.setOnClickListener(v -> {
            List<Station> l = trainDAO.getStations().blockingGet();
            Log.i("TAG", "onViewCreated: " + l.size());
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,stationFragment)
                    .addToBackStack(null)

                    .commit();
        });

        trains.setOnClickListener(v -> {
            List<Train> l = trainDAO.getTrains().blockingGet();
            Log.i("TAG", "onViewCreated: " + l.size());

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host,trainFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}