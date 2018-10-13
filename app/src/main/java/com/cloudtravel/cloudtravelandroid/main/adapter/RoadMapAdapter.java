package com.cloudtravel.cloudtravelandroid.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.item.ScheduleItem;

import java.util.List;

public class RoadMapAdapter extends RecyclerView.Adapter<RoadMapAdapter.ViewHolder> {
    private List<ScheduleItem> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView time;
        TextView name;
        TextView address;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image_view);
            time = view.findViewById(R.id.time_name);
            name = view.findViewById(R.id.place_name);
            address=view.findViewById(R.id.place_address);
        }
    }

    public RoadMapAdapter(List<ScheduleItem> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.road_map_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleItem scheduleItem = mList.get(position);
        holder.img.setImageResource(scheduleItem.getTypeIconId());
        holder.name.setText(scheduleItem.getPlaceName());
        holder.time.setText(scheduleItem.getTime());
        holder.address.setText(scheduleItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}