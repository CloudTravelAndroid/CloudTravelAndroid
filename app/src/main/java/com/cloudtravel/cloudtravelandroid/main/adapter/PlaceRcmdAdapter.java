package com.cloudtravel.cloudtravelandroid.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.activity.WebViewActivity;
import com.cloudtravel.cloudtravelandroid.main.item.PlaceRcmdItem;

import java.util.List;

/**
 * Created by Brandon on 2018/3/11.
 */

public class PlaceRcmdAdapter extends RecyclerView.Adapter<PlaceRcmdAdapter.ViewHolder> {
    private Context mContext;
    private List<PlaceRcmdItem> mRcmdList;

    public PlaceRcmdAdapter(List<PlaceRcmdItem> rcmdList) {
        mRcmdList = rcmdList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.place_rcmd_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PlaceRcmdItem placeRcmdItem = mRcmdList.get(position);
        holder.rcmdTitle.setText(placeRcmdItem.getTitle());
        Glide.with(mContext).load(placeRcmdItem.getImageId()).into(holder.rcmdImage);
        //holder.rcmdImage.setImageResource(placeRcmdItem.getImageId());
        /*Drawable drawable=holder.rcmdImage.getDrawable();
        if (drawable!=null) {
            drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
        }*/
        float brightness = -50f;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        holder.rcmdImage.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        holder.rcmdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", placeRcmdItem.getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRcmdList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView rcmdImage;
        TextView rcmdTitle;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            rcmdImage = view.findViewById(R.id.rcmd_image);
            rcmdTitle = view.findViewById(R.id.rcmd_title);
        }
    }
}
