package com.cloudtravel.cloudtravelandroid.main.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.main.adapter.SearchResultItemAdapter;
import com.cloudtravel.cloudtravelandroid.main.item.SearchResultItem;
import com.cloudtravel.cloudtravelandroid.main.util.PoiOverlay;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends CloudTravelBaseActivity {

    private static final String TAG = "SearchActivity";
    OnGetPoiSearchResultListener poiListener;
    SearchResultItemAdapter adapter;
    private LocationClient mLocationClient;
    private PoiSearch mPoiSearch;
    private BDLocation currentLocation;
    private ImageView backImage;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private SearchView searchView;
    private PopupWindow mPopupWindow;
    private RecyclerView recyclerView;
    private View rootView;
    private boolean isFirstLocate = true;
    private List<SearchResultItem> searchResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
                        || bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    currentLocation = bdLocation;
                    //Log.e(TAG, "onReceiveLocation: bdLocation:"+bdLocation.getLatitude()+" "+bdLocation.getLongitude() );
                    navigateTo(bdLocation);
                }
            }
        });
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_search);
        rootView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.activity_search, null);
        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);
        mapView = (MapView) findViewById(R.id.baidumap_view);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mPoiSearch = PoiSearch.newInstance();
        poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                mBaiduMap.clear();
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(SearchActivity.this, "搜索出现错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (poiResult.error == PoiResult.ERRORNO.NO_ERROR) {
                    PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(poiResult);
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    List<PoiInfo> poiInfoList = poiResult.getAllPoi();
                    for (PoiInfo poiInfo : poiInfoList) {
                        SearchResultItem item = new SearchResultItem();
                        item.setAddress(poiInfo.address);
                        item.setName(poiInfo.name);
                        item.setUid(poiInfo.uid);
                        searchResultList.add(item);
                        adapter.notifyDataSetChanged();
                        showPopup();
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                searchResultList.clear();
                LatLng center = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                int radius = 10000;
                mPoiSearch.searchNearby(new PoiNearbySearchOption()
                        .keyword(query).sortType(PoiSortType.distance_from_near_to_far)
                        .location(center).radius(radius).pageCapacity(20).pageNum(0));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        initView();

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SearchActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mPoiSearch.destroy();
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

    public void initView() {
        super.hideTitleBar();
        initPopupWindow();
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll, 16f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    private void initPopupWindow() {
        View contentView = getLayoutInflater().inflate(R.layout.search_result_layout, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, 1000);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 255, 255, 255)));
        mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
        mPopupWindow.update();
        recyclerView = contentView.findViewById(R.id.search_result_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchResultItemAdapter(searchResultList);
        recyclerView.setAdapter(adapter);
    }

    private void showPopup() {
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    private void hideKeyboard(View v) {
        InputMethodManager manager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    private class MyPoiOverlay extends PoiOverlay {
        private MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
            String uid = getPoiResult().getAllPoi().get(i).uid;
            Intent intent = new Intent(SearchActivity.this, PlaceDetailedActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            return true;
        }
    }
}

