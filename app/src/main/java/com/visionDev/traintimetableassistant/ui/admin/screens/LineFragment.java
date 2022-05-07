package com.visionDev.traintimetableassistant.ui.admin.screens;

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
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visionDev.traintimetableassistant.ui.admin.AdminActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Line;
import com.visionDev.traintimetableassistant.ui.admin.adapters.LineRecyclerViewAdapter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LineFragment extends Fragment {


    public LineFragment() {
    }


    public static LineFragment newInstance() {
        LineFragment fragment = new LineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_list, container, false);
    }

    CompositeDisposable cd = new CompositeDisposable();
    LineRecyclerViewAdapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
         adapter = new LineRecyclerViewAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        FloatingActionButton fb = view.findViewById(R.id.floatingActionButton3);

        fb.setOnClickListener(w->{
            final AlertDialog.Builder b = new AlertDialog.Builder(requireActivity());
            View v = getLayoutInflater().inflate(R.layout.dialog_add_line,null,false);
             b.setView(v);
             EditText lnet = v.findViewById(R.id.station_name_e);
             EditText lnoet = v.findViewById(R.id.line_number_e);

            b.setPositiveButton(R.string.submit,(d,z)->{
                String name =  lnet.getText().toString();
                int no = Integer.parseInt( lnoet.getText().toString());
                Line n = new Line(no,name);
                cd.add(
                ((AdminActivity) requireActivity()).db.getTrainDAO().addLine(n)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(id -> n.id  = id)
                );

                d.dismiss();
            });
             b.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cd.add(
         ((AdminActivity) requireActivity()).db.getTrainDAO().observeLines()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(l-> adapter.setLines(l))
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        cd.clear();
    }
}