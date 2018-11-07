package com.cloudtravel.cloudtravelandroid.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseFragment;
import com.cloudtravel.cloudtravelandroid.main.adapter.MomentsItemAdapter;
import com.cloudtravel.cloudtravelandroid.main.item.MomentsItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MomentsFragment extends CloudTravelBaseFragment {

    private RecyclerView recyclerViewMoments;
    private TextView nameText;
    private CircleImageView headPortraitImage;
    private List<MomentsItem> momentItemList = new ArrayList<>();

    public MomentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moments, container, false);
        getMoments();
        recyclerViewMoments = view.findViewById(R.id.moments);
        nameText = view.findViewById(R.id.moments_name);
        headPortraitImage = view.findViewById(R.id.moments_item_head_portrait);
        recyclerViewMoments = view.findViewById(R.id.moments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewMoments.setLayoutManager(layoutManager);
        MomentsItemAdapter momentItemAdapter = new MomentsItemAdapter(momentItemList);
        recyclerViewMoments.setAdapter(momentItemAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getMoments() {
//        for (int i = 0; i < 4; i++) {
//            MomentsItem momentItem = new MomentsItem();
//            momentItem.setContent("It was an impromptu call up to Shanghai but nevertheless, I had a " +
//                    "worthwhile trip to QiBao Ancient Watertown. There is lots of photo opportunity found " +
//                    "in this quaint little thousand year old town, where you can still find antique buildings" +
//                    " from the Northern Song Dynasty. May I suggest having an empty stomach when you visit " +
//                    "this ancient water town. If not, you will be missing out on some good local snacks. It " +
//                    "was so good that I had two baskets by myself. I am a Xiao Long Bao’s lover! I " +
//                    "am a Chinese and I like my food to be served pipping hot! I always miss our Asian food " +
//                    "when I am in European countries. And an interesting thing that I have noticed is that " +
//                    "they do not provide shredded ginger and spoon in China. So…Does that mean the authentic " +
//                    "way of eating Xiao Long Bao is without shredded ginger and spoon?");
//            momentItem.setCreateTime(new Date());
//            momentItem.setLocation("Shanghai, China");
//            momentItem.setName("Irene");
//            momentItemList.add(momentItem);
//        }

    }
}
