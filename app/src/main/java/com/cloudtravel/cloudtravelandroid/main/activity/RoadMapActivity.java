package com.cloudtravel.cloudtravelandroid.main.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.main.RoadMapDecoration;
import com.cloudtravel.cloudtravelandroid.main.adapter.RoadMapAdapter;
import com.cloudtravel.cloudtravelandroid.main.item.ScheduleItem;
import com.cloudtravel.cloudtravelandroid.main.util.GeoUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoadMapActivity extends CloudTravelBaseActivity {

    private static final int[] COLORS = {0xAAFF0000, 0xAA00FF00, 0xAA0000FF, 0xAA00FFFF};
    private List<ScheduleItem> scheduleItemList;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private List<LatLng> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_road_map);
        super.hideTitleBar();
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        Button button = findViewById(R.id.bt_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapView = findViewById(R.id.bmapView);
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
        //MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        //MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.clear();

        scheduleItemList = getIntent().getParcelableArrayListExtra("list");

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap mapMarker = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.map_marker, options), 128, 128, false);
        BitmapDescriptor marker = BitmapDescriptorFactory.fromBitmap(mapMarker);
        points = new ArrayList<>();
        List<OverlayOptions> markerList = new ArrayList<>();
        for (ScheduleItem scheduleItem : scheduleItemList) {
            LatLng latLng = new LatLng(scheduleItem.getLatitude(), scheduleItem.getLongitude());
            points.add(latLng);
            OverlayOptions overlayOptions = new MarkerOptions().position(latLng).icon(marker)
                    .title(scheduleItem.getPlaceName()).animateType(MarkerOptions.MarkerAnimateType.grow);
            OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(36).fontColor(0xFFFF00FF)
                    .text(scheduleItem.getPlaceName()).position(latLng);
            mBaiduMap.addOverlay(textOption);
            markerList.add(overlayOptions);
        }
        zoomToSpan();
        mBaiduMap.addOverlays(markerList);
        if (scheduleItemList.size() > 1) {
            List<Integer> colors = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                colors.add(COLORS[(int) (Math.random() * 4)]);
            }
            OverlayOptions polyLine = new PolylineOptions().width(10).colorsValues(colors).points(points);
            mBaiduMap.addOverlay(polyLine);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RoadMapAdapter tda = new RoadMapAdapter(scheduleItemList);
        recyclerView.setAdapter(tda);
        recyclerView.addItemDecoration(new RoadMapDecoration());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void zoomToSpan() {
        List<Double> latitudeList = new ArrayList<>();
        List<Double> longitudeList = new ArrayList<>();
        for (LatLng latLng : points) {
            latitudeList.add(latLng.latitude);
            longitudeList.add(latLng.longitude);
        }
        double maxLatitude = Collections.max(latitudeList);
        double maxLongitude = Collections.max(longitudeList);
        double minLatitude = Collections.min(latitudeList);
        double minLongitude = Collections.min(longitudeList);
        double distance = GeoUtil.GetDistance(maxLatitude, maxLongitude, minLatitude, minLongitude);

        float level;
        int zoom[] = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 1000, 2000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000};
        for (int i = 0; i < zoom.length; i++) {
            int zoomNow = zoom[i];
            if (zoomNow - distance * 1000 > 0) {
                level = 18 - i + 6;
                //设置地图显示级别为计算所得level
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(level).build()));
                break;
            }
        }

        LatLng center = new LatLng((maxLatitude + minLatitude) / 2, (maxLongitude + minLongitude) / 2);
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(center);
        mBaiduMap.animateMapStatus(statusUpdate, 100);
    }
}
