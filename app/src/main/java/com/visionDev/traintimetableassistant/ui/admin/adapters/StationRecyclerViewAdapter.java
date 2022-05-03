package com.visionDev.traintimetableassistant.ui.admin.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Station;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Station}.
 */
public class StationRecyclerViewAdapter extends RecyclerView.Adapter<StationRecyclerViewAdapter.ViewHolder> {

    private final List<Station> mValues;

    public StationRecyclerViewAdapter(List<Station> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_station, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.stationName.setText(mValues.get(position).name);
        holder.totalPlatforms.setText("No.Of.Platforms : "+mValues.get(position).noOfPlatforms);
        holder.lineName.setText("Line No.  : "+mValues.get(position).lineId + "");
    }


    public void addStation(Station s){
        mValues.add(s);
        notifyItemInserted(mValues.size()-1);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView stationName;
        public final TextView totalPlatforms;
        public final TextView lineName;

        public Station mItem;

        public ViewHolder(View v) {
            super(v);
        stationName = v.findViewById(R.id.station_name);
        totalPlatforms  = v.findViewById(R.id.total_platforms);
        lineName = v.findViewById(R.id.line_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + stationName.getText() + "'";
        }
    }
}