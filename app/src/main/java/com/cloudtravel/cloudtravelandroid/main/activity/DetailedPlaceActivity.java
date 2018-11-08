package com.cloudtravel.cloudtravelandroid.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.bumptech.glide.Glide;
import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;

import java.util.List;

public class DetailedPlaceActivity extends CloudTravelBaseActivity {
    private static final String TAG = "DetailedPlaceActivity";

    private ImageView collectImage;
    private ImageView searchAroundImage;
    private ImageView scheduleImage;
    private PoiSearch mPoiSearch;
    private OnGetPoiSearchResultListener poiListener;
    private TextView placeNameText;
    private TextView placeAddressText;
    private TextView placeIntroductionText;
    private ImageView placeImage;
    private RatingBar ratingBar;
    private LatLng latLng;
    private String placeName;
    private String placeAddress;
    private String placeUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detailed);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        placeImage = findViewById(R.id.place_image_view);
        collectImage = findViewById(R.id.collect_image_view);
        searchAroundImage = findViewById(R.id.search_around_image_view);
        scheduleImage = findViewById(R.id.schedule_image_view);
        placeNameText = findViewById(R.id.schedule_place_name_text_view);
        placeAddressText = findViewById(R.id.place_address_text_view);
        placeIntroductionText = findViewById(R.id.introduction_text_view);
        ratingBar = findViewById(R.id.rating_bar);
        scheduleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedPlaceActivity.this, AddScheduleActivity.class);
                intent.putExtra("name", placeName);
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                intent.putExtra("address", placeAddress);
                intent.putExtra("uid", placeUID);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mPoiSearch = PoiSearch.newInstance();
        poiListener = new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
                if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(DetailedPlaceActivity.this, "获取详情失败",
                            Toast.LENGTH_SHORT).show();
                } else {
                    List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult
                            .getPoiDetailInfoList();
                    if (poiDetailInfoList == null || poiDetailInfoList.isEmpty()) {
                        Toast.makeText(DetailedPlaceActivity.this, "检索结果为空",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(0);
                    if (poiDetailInfo != null) {
                        if (poiDetailInfo.getStreetId() != null
                                && !poiDetailInfo.getStreetId().isEmpty()) {
                            Glide.with(DetailedPlaceActivity.this).
                                    load(poiDetailInfo.getStreetId()).into(placeImage);
                        }
                        placeNameText.setText(poiDetailInfo.getName());
                        placeAddressText.setText(poiDetailInfo.getAddress());
                        StringBuffer intro = new StringBuffer();
                        if (!"".equals(poiDetailInfo.getTag())) {
                            intro.append(poiDetailInfo.getTag() + "\n");
                        } else {
                            intro.append("暂无简介\n");
                        }
                        if (!"".equals(poiDetailInfo.getShopHours())) {
                            intro.append("营业时间: " + poiDetailInfo.getShopHours());
                        }
                        placeIntroductionText.setText(intro.toString());
                        float rating = (float) poiDetailInfo.getOverallRating();
                        ratingBar.setRating(rating);
                        latLng = poiDetailInfo.getLocation();
                        placeName = poiDetailInfo.getName();
                        placeAddress = poiDetailInfo.getAddress();
                        placeUID = poiDetailInfo.getUid();
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }

            @Override
            public void onGetPoiResult(PoiResult poiResult) {
            }

        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        String uid = getIntent().getStringExtra("uid");
        mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUids(uid));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
