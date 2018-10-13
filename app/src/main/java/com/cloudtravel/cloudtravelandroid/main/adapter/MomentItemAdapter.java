package com.cloudtravel.cloudtravelandroid.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.item.MomentItem;
import com.lemon.support.util.DateUtil;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MomentItemAdapter extends RecyclerView.Adapter<MomentItemAdapter.ViewHolder>{
    private List<MomentItem> mMomentItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headPortraitImage;
        TextView nameText;
        TextView contentText;
        TextView timeText;
        TextView locationText;

        public ViewHolder(View view) {
            super(view);
            headPortraitImage=view.findViewById(R.id.moments_item_head_portrait);
            nameText=view.findViewById(R.id.moments_item_name);
            contentText=view.findViewById(R.id.moments_item_content);
            timeText=view.findViewById(R.id.moments_item_time);
            locationText=view.findViewById(R.id.moments_item_location);
        }
    }

    public MomentItemAdapter(List<MomentItem> momentItemList) {
        mMomentItemList=momentItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.moments_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MomentItem momentItem=mMomentItemList.get(position);
        holder.locationText.setText(momentItem.getLocation());
        //DateFormat dateFormat= DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
        //holder.timeText.setText(dateFormat.format(new Date()));
        holder.timeText.setText(DateUtil.date2Str(new Date(),"YYYY-MM-dd HH:mm"));
        holder.contentText.setText(momentItem.getContent());
        holder.nameText.setText(momentItem.getName());
        //to be continued...

    }

    @Override
    public int getItemCount() {
        return mMomentItemList.size();
    }
}