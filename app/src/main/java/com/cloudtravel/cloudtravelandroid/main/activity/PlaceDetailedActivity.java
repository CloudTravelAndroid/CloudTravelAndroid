package com.cloudtravel.cloudtravelandroid.main.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;

public class PlaceDetailedActivity extends CloudTravelBaseActivity {
    private static final String TAG = "PlaceDetailedActivity";

    private ImageView collectImage;
    private ImageView searchAroundImage;
    private ImageView scheduleImage;
    private PoiSearch mPoiSearch;
    private OnGetPoiSearchResultListener poiListener;
    private TextView placeNameText;
    private TextView placeAddressText;
    private TextView placeIntroductionText;
    private RatingBar ratingBar;
    private LatLng latLng;
    private String placeName;
    private String placeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detailed);
        initView();
    }

    private void  initView() {
        super.hideTitleBar();
        Toolbar toolbar = findViewById(R.id.toolbar);
        collectImage = findViewById(R.id.collect_image_view);
        searchAroundImage = findViewById(R.id.search_around_image_view);
        scheduleImage = findViewById(R.id.schedule_image_view);
        placeNameText=findViewById(R.id.schedule_place_name_text_view);
        placeAddressText=findViewById(R.id.place_address_text_view);
        placeIntroductionText=findViewById(R.id.introduction_text_view);
        ratingBar=findViewById(R.id.rating_bar);
        scheduleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceDetailedActivity.this,AddScheduleActivity.class);
                intent.putExtra("name",placeName);
                intent.putExtra("lat",latLng.latitude);
                intent.putExtra("lng",latLng.longitude);
                intent.putExtra("address",placeAddress);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        /*
        GetLocationInfoRequest request=new GetLocationInfoRequest();
        addRequest(getService(GetLocationInfoApi.class).doGetLocationInfo(request), new CloudTravelBaseCallBack() {
            @Override
            public void onSuccess200(Object o) {
                Location location= GsonUtil.getEntity(o,new TypeToken<Location>(){}.getType());

            }
        });
        */
        mPoiSearch=PoiSearch.newInstance();
        poiListener=new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    placeNameText.setText(poiDetailResult.name);
                    placeAddressText.setText(poiDetailResult.address);
                    StringBuilder intro=new StringBuilder();
                    if (!"".equals(poiDetailResult.getTag())) {
                        intro.append(poiDetailResult.getTag()+"\n");
                    } else {
                        intro.append("暂无简介\n");
                    }
                    if (!"".equals(poiDetailResult.getShopHours())) {
                        intro.append("营业时间: " + poiDetailResult.getShopHours());
                    }
                    placeIntroductionText.setText(intro.toString());
                    float rating=(float)poiDetailResult.getOverallRating();
                    //Log.e(TAG,"rating: "+rating);
                    ratingBar.setRating(rating);
                    latLng=poiDetailResult.location;
                    placeName=poiDetailResult.name;
                    placeAddress=poiDetailResult.address;
                } else {
                    Toast.makeText(PlaceDetailedActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        String uid=getIntent().getStringExtra("uid");
        mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
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
