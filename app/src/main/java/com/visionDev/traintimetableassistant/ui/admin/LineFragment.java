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

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.ui.admin.adapters.LineRecyclerViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class LineFragment extends Fragment {


    private static final String ARG_LINES = "rail-lines";

    private List<Line> mLines;

    public LineFragment() {
    }


    public static LineFragment newInstance(ArrayList<Line> lines) {
        LineFragment fragment = new LineFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LINES,lines);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable arg =  requireArguments().getSerializable(ARG_LINES);
        if(arg instanceof ArrayList){
            mLines = (ArrayList<Line>) arg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        LineRecyclerViewAdapter adapter = new LineRecyclerViewAdapter(mLines);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }


}