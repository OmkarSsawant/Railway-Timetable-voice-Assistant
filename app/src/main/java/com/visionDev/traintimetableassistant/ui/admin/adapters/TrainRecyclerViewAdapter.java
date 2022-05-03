package com.visionDev.traintimetableassistant.ui.admin.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Train}.
 */
public class TrainRecyclerViewAdapter extends RecyclerView.Adapter<TrainRecyclerViewAdapter.ViewHolder> {

    private final List<Train> mValues;

    public TrainRecyclerViewAdapter(List<Train> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_train_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.start.setText(mValues.get(position).startStationId+"");
        holder.end.setText(mValues.get(position).endStationId+"");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView start;
        public final TextView end;

        public Train mItem;

        public ViewHolder(View v) {
            super(v);
            start = v.findViewById(R.id.start_station);
            end = v.findViewById(R.id.end_station);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + start.getText()  + "-" + end.getText();
        }
    }
}