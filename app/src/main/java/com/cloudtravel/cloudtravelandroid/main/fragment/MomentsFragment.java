package com.cloudtravel.cloudtravelandroid.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseFragment;
import com.cloudtravel.cloudtravelandroid.main.activity.CreateMomentsActivity;
import com.cloudtravel.cloudtravelandroid.main.activity.DetailedMomentsActivity;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.dto.MomentsDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.UserDTO;
import com.cloudtravel.cloudtravelandroid.main.item.MomentsItem;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;
import com.lemon.support.util.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MomentsFragment extends CloudTravelBaseFragment
        implements BGANinePhotoLayout.Delegate {

    private RecyclerView recyclerViewMoments;
    private TextView nameText;
    private CircleImageView headPortraitImage;
    private final int SIZE = 15;

    private List<MomentsItem> momentItemList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private MomentsAdapter momentsAdapter;

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
        floatingActionButton = view.findViewById(R.id.post_moments_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateMomentsActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewMoments.setLayoutManager(layoutManager);
        momentsAdapter = new MomentsAdapter(recyclerViewMoments);
        recyclerViewMoments.setAdapter(momentsAdapter);
        getUsername();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMoments();
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position,
                                     String model, List<String> models) {
        File downloadDir = new File(Environment.getExternalStorageDirectory(),
                "BGAPhotoPickerDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new
                BGAPhotoPreviewActivity.IntentBuilder(getContext()).saveImgDir(downloadDir);
        if (ninePhotoLayout.getItemCount() == 1) {
            photoPreviewIntentBuilder.previewPhoto(ninePhotoLayout.getCurrentClickItem());
        } else if (ninePhotoLayout.getItemCount() > 1) {
            photoPreviewIntentBuilder.previewPhotos(ninePhotoLayout.getData())
                    .currentPosition(ninePhotoLayout.getCurrentClickItemPosition());
        }
        startActivity(photoPreviewIntentBuilder.build());
    }

    private void getMoments() {
        Call<BaseResponse<List<MomentsDTO>>> call = CloudTravelService.getInstance()
                .getLatestMoments(SIZE);
        call.enqueue(new Callback<BaseResponse<List<MomentsDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MomentsDTO>>> call,
                                   Response<BaseResponse<List<MomentsDTO>>> response) {
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
                            momentsAdapter.setData(momentItemList);
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

    private void getUsername() {
        Call<BaseResponse<UserDTO>> call = CloudTravelService.getInstance().getUserInfo();
        call.enqueue(new Callback<BaseResponse<UserDTO>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserDTO>> call, Response<BaseResponse<UserDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        UserDTO userDTO = response.body().getObject();
                        nameText.setText(userDTO.getName());
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MomentsAdapter extends BGARecyclerViewAdapter<MomentsItem> {

        public MomentsAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.moments_item);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, MomentsItem momentsItem) {
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_moment_photos);
            ninePhotoLayout.setDelegate(MomentsFragment.this);
            ninePhotoLayout.setData((ArrayList<String>) momentsItem.getImageUrlList());
            helper.setText(R.id.moments_item_name, momentsItem.getUsername());
            helper.setText(R.id.moments_item_content, momentsItem.getContent());
            helper.setText(R.id.moments_item_time, DateUtil.date2Str(momentsItem.getTime(),
                    "YYYY-MM-dd HH:mm"));
            final MomentsItem item = momentsItem;
            helper.getView(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ;
                    Intent intent = new Intent(getContext(), DetailedMomentsActivity.class);
                    intent.putExtra(DetailedMomentsActivity.USERNAME,
                            nameText.getText().toString());
                    intent.putExtra(DetailedMomentsActivity.NAME, item.getUsername());
                    intent.putExtra(DetailedMomentsActivity.TIME, item.getTime());
                    intent.putExtra(DetailedMomentsActivity.CONTENT, item.getContent());
                    intent.putExtra(DetailedMomentsActivity.ID, item.getMomentId());
                    intent.putStringArrayListExtra(DetailedMomentsActivity.PHOTOS,
                            (ArrayList<String>) item.getImageUrlList());
                    startActivity(intent);
                }
            });
        }
    }
}
