package com.visionDev.traintimetableassistant.ui.admin.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.models.Train;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Train}.
 */
public class TrainRecyclerViewAdapter extends RecyclerView.Adapter<TrainRecyclerViewAdapter.ViewHolder> {

    private final List<Train> mValues;
    private final TrainDAO dao;
    private final List<Station> stations;
    public TrainRecyclerViewAdapter(List<Train> items,TrainDAO trainDAO) {
        mValues = items;
        dao = trainDAO;
        stations  = dao.getStations().blockingGet();
    }


    String getStationName(Long stationNo){
        for (Station s:
             stations) {
            if(s.stationNo == stationNo)
                return  s.name;
        }
        return  "Unknown";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_train_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.start.setText(getStationName(mValues.get(position).startStationId));
        holder.end.setText(getStationName(mValues.get(position).endStationId));
        holder.delete.setOnClickListener(v->{
                dao.deleteTrain(holder.mItem);
                int i = mValues.indexOf(holder.mItem);
                mValues.remove(i);
                notifyItemRemoved(i);
            Toast.makeText(v.getContext(),"Deleted Train",Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView start;
        public final TextView end;
        public final Button delete;
        public Train mItem;

        public ViewHolder(View v) {
            super(v);
            start = v.findViewById(R.id.start_station);
            end = v.findViewById(R.id.end_station);
            delete = v.findViewById(R.id.delete_train);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + start.getText()  + "-" + end.getText();
        }
    }
}