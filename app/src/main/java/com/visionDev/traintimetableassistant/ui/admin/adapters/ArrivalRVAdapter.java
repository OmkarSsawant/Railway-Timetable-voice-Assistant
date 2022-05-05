package com.visionDev.traintimetableassistant.ui.admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Arrival;
import com.visionDev.traintimetableassistant.data.models.Station;
import com.visionDev.traintimetableassistant.data.room.TrainDAO;

import java.util.ArrayList;
import java.util.List;

public class ArrivalRVAdapter extends RecyclerView.Adapter<ArrivalRVAdapter.ArrivalViewHolder> {


    ArrayList<Arrival> arrivals = new ArrayList<>();
    private final List<Station> stations;

    String getStationName(Long stationNo){
        for (Station s:
                stations) {
            if(s.stationNo == stationNo)
                return  s.name;
        }
        return  "Unknown";
    }

    public  ArrivalRVAdapter(TrainDAO dao){
        stations  = dao.getStations().blockingGet();
    }

    @NonNull
    @Override
    public ArrivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArrivalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_train, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArrivalViewHolder holder, int position) {

        Arrival a = arrivals.get(position);
        holder.stationName.setText(getStationName(a.station_id));
        holder.arrivalTime.setText(a.arrivalTime.toString());
        holder.pfn.setText(a.platformNumber+"");

    }

    @Override
    public int getItemCount() {
        return arrivals.size();
    }

   public void addArrivals(Arrival a){
      arrivals.add(a);
      notifyItemInserted(arrivals.size()-1);
    }

    static class ArrivalViewHolder extends RecyclerView.ViewHolder {
        TextView stationName, pfn,arrivalTime;

        public ArrivalViewHolder(@NonNull View itemView) {
            super(itemView);
            stationName  = itemView.findViewById(R.id.arr_station_name);
            pfn = itemView.findViewById(R.id.arr_pfn);
            arrivalTime  = itemView.findViewById(R.id.arr_time);
        }
    }
}
