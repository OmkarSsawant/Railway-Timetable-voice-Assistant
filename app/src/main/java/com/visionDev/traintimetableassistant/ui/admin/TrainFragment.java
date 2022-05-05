package com.visionDev.traintimetableassistant.ui.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visionDev.traintimetableassistant.MainActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.ui.admin.adapters.TrainRecyclerViewAdapter;

import java.util.ArrayList;


public class TrainFragment extends Fragment {


    private ArrayList<Train> trains = new ArrayList<>();
    public TrainFragment() {
    }


    public static TrainFragment newInstance(ArrayList<Train> trains) {
        TrainFragment fragment = new TrainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
         adapter = new TrainRecyclerViewAdapter(trains,((MainActivity)requireActivity()).db.getTrainDAO());
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

    @Override
    public void onResume() {
        super.onResume();
        trains = new ArrayList<>(((MainActivity) requireActivity()).db.getTrainDAO().getTrains().blockingGet());
        adapter.setTrains(trains);
    }
}