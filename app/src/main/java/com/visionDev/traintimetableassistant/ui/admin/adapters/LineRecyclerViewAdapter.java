package com.visionDev.traintimetableassistant.ui.admin.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visionDev.traintimetableassistant.R;
import com.visionDev.traintimetableassistant.data.models.Line;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Line}.

 */
public class LineRecyclerViewAdapter extends RecyclerView.Adapter<LineRecyclerViewAdapter.ViewHolder> {

    private final List<Line> mValues;

    public LineRecyclerViewAdapter(List<Line> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_line, parent, false));

    }


   public void addLine(Line n){
        mValues.add(n);
        notifyItemInserted(mValues.size()-1);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).id+"");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Line mItem;

        public ViewHolder(View v) {
            super(v);
            mIdView = v.findViewById(R.id.line_name);
            mContentView = v.findViewById(R.id.line_number);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}