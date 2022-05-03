package com.visionDev.traintimetableassistant.ui.admin.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.visionDev.traintimetableassistant.MainActivity;
import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.ui.admin.StationFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Station}.
 */
public class StationRecyclerViewAdapter extends RecyclerView.Adapter<StationRecyclerViewAdapter.ViewHolder> {

    private final List<Station> mValues;
    private final MainActivity mainActivity;

    public StationRecyclerViewAdapter(List<Station> items,MainActivity mainActivity) {
        mValues = items;
        this.mainActivity = mainActivity;
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
        holder.edit.setOnClickListener(view -> {
            StationFragment.showAlertDialog(holder.mItem,mainActivity.getLayoutInflater(),mainActivity,this);
        });
        holder.delete.setOnClickListener(view ->
        {
             if(mainActivity.db.getTrainDAO().deleteStation(holder.mItem) != -1){
                 int i = mValues.indexOf(holder.mItem);
                 mValues.remove(holder.mItem);
                 notifyItemRemoved(i);
             }
        });
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
        public final Button edit;
        public final Button delete;


        public Station mItem;

        public ViewHolder(View v) {
            super(v);
        stationName = v.findViewById(R.id.station_name);
        totalPlatforms  = v.findViewById(R.id.total_platforms);
        lineName = v.findViewById(R.id.line_name);
            edit = v.findViewById(R.id.edit);
            delete = v.findViewById(R.id.delete);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + stationName.getText() + "'";
        }
    }
}