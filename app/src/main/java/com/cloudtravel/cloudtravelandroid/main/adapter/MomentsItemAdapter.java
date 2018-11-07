package com.cloudtravel.cloudtravelandroid.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.item.MomentsItem;
import com.lemon.support.util.DateUtil;

import java.util.Date;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class MomentsItemAdapter extends RecyclerView.Adapter<MomentsItemAdapter.ViewHolder> {
    private List<MomentsItem> mMomentItemList;

    public MomentsItemAdapter(List<MomentsItem> momentItemList) {
        mMomentItemList = momentItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moments_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MomentsItem momentItem = mMomentItemList.get(position);
        holder.timeText.setText(DateUtil.date2Str(new Date(), "YYYY-MM-dd HH:mm"));
        holder.contentText.setText(momentItem.getContent());
        holder.nameText.setText(momentItem.getUsername());

    }

    @Override
    public int getItemCount() {
        return mMomentItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView headPortraitImage;
        TextView nameText;
        TextView contentText;
        TextView timeText;
        BGANinePhotoLayout ninePhotoLayout;

        public ViewHolder(View view) {
            super(view);
            headPortraitImage = view.findViewById(R.id.moments_item_head_portrait);
            nameText = view.findViewById(R.id.moments_item_name);
            contentText = view.findViewById(R.id.moments_item_content);
            timeText = view.findViewById(R.id.moments_item_time);
            ninePhotoLayout = view.findViewById(R.id.npl_item_moment_photos);
        }
    }
}