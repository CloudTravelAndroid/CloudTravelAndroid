package com.cloudtravel.cloudtravelandroid.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseFragment;
import com.cloudtravel.cloudtravelandroid.main.adapter.MomentsItemAdapter;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.dto.MomentsDTO;
import com.cloudtravel.cloudtravelandroid.main.item.MomentsItem;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MomentsFragment extends CloudTravelBaseFragment {

    private RecyclerView recyclerViewMoments;
    private TextView nameText;
    private CircleImageView headPortraitImage;
    private List<MomentsItem> momentItemList = new ArrayList<>();
    private final int size = 15;
    MomentsItemAdapter momentItemAdapter;

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
        momentItemAdapter = new MomentsItemAdapter(momentItemList);
        recyclerViewMoments.setAdapter(momentItemAdapter);
        getMoments();
        return view;
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

        Call<BaseResponse<List<MomentsDTO>>> call = CloudTravelService.getInstance()
                .getLatestMoments(size);
        call.enqueue(new Callback<BaseResponse<List<MomentsDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MomentsDTO>>> call, Response<BaseResponse<List<MomentsDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<MomentsDTO> momentsDTOS = response.body().getObject();
                        if (momentsDTOS != null && !momentsDTOS.isEmpty()) {
                            momentItemList.clear();
                            for (MomentsDTO momentsDTO : momentsDTOS) {
                                MomentsItem momentsItem = new MomentsItem();
                                momentsItem.setContent(momentsDTO.getContent());
                                momentsItem.setTime(momentsDTO.getTime());
                                momentsItem.setUniversity(momentsDTO.getUniversity());
                                momentsItem.setUsername(momentsDTO.getUsername());
                                momentsItem.setImageUrlList(momentsDTO.getImageUrls());
                                momentItemList.add(momentsItem);
                            }
                            momentItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MomentsDTO>>> call, Throwable t) {
                Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
