package com.cloudtravel.cloudtravelandroid.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.item.ScheduleItem;
import com.lemon.support.util.DateUtil;

import java.util.Date;
import java.util.List;

public class ScheduleItemAdapter extends RecyclerView.Adapter<ScheduleItemAdapter.ViewHolder> {
    private List<ScheduleItem> mList;
    private OnDeleteItemClickListener mDeleteListener;
    private OnUpdateItemClickListener mUpdateListener;

    public ScheduleItemAdapter(List<ScheduleItem> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ScheduleItem schedule = mList.get(position);
        holder.placeType.setImageResource(schedule.getTypeIconId());
        holder.placeName.setText(schedule.getPlaceName());
        Date date = DateUtil.str2Date(schedule.getTime());
        holder.scheduleTime.setText(DateUtil.date2Str(date, "HH:mm"));
        if (mDeleteListener != null) {
            holder.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteListener.onItemClick(v, position);
                }
            });
        }
        if (mUpdateListener != null) {
            holder.editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUpdateListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        mDeleteListener = listener;
    }

    public void setOnUpdateItemClickListener(OnUpdateItemClickListener listener) {
        mUpdateListener = listener;
    }

    public interface OnDeleteItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnUpdateItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeType;
        TextView scheduleTime;
        TextView placeName;
        ImageView deleteImage;
        ImageView editImage;

        public ViewHolder(View view) {
            super(view);
            placeType = (ImageView) view.findViewById(R.id.place_type_image_view);
            scheduleTime = (TextView) view.findViewById(R.id.schedule_time_text_view);
            placeName = (TextView) view.findViewById(R.id.schedule_place_name_text_view);
            deleteImage = view.findViewById(R.id.schedule_item_delete_image_view);
            editImage = view.findViewById(R.id.schedule_item_edit_image_view);
        }
    }
}
