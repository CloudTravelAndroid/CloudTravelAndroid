package com.cloudtravel.cloudtravelandroid.main.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseFragment;
import com.cloudtravel.cloudtravelandroid.main.activity.SearchActivity;
import com.cloudtravel.cloudtravelandroid.main.adapter.PlaceRcmdAdapter;
import com.cloudtravel.cloudtravelandroid.main.item.PlaceRcmdItem;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends CloudTravelBaseFragment {

    private static final String TAG = "DiscoverFragment";

    private List<Integer> bannerImages;
    private List<String> bannerTitles;
    private Banner banner;
    private TextView search_layout;
    private List<PlaceRcmdItem> placeRcmdItemList = new ArrayList<>();
    private PlaceRcmdAdapter adapter;
    private RecyclerView recyclerView;
    private TextView currentCityText;

    private LocationClient mLocationClient;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        search_layout = view.findViewById(R.id.search_bar);
        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.banner_0);
        bannerImages.add(R.drawable.banner_1);
        bannerImages.add(R.drawable.banner_2);
        bannerImages.add(R.drawable.banner_3);
        bannerImages.add(R.drawable.banner_4);
        bannerImages.add(R.drawable.banner_5);
        bannerImages.add(R.drawable.banner_6);
        bannerImages.add(R.drawable.banner_7);
        bannerTitles = new ArrayList<>();
        for (int i = 0; i < bannerImages.size(); i++) {
            bannerTitles.add("");
        }
        banner = view.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(bannerImages);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setBannerTitles(bannerTitles);
        banner.isAutoPlay(true);
        banner.setDelayTime(6000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();

        placeRcmdItemList.add(new PlaceRcmdItem("Zhenning Yang in ECNU",
                "Shanghai", R.drawable.yzn, "http://www.smartshanghai.com/listings/dining/dim_sum/"));
        placeRcmdItemList.add(new PlaceRcmdItem("67th Anniversary of ECNU",
                "Shanghai", R.drawable.anniversary, "www.pretty-hotels.com/2248/?lang=en"));
        placeRcmdItemList.add(new PlaceRcmdItem("2018 Sports Meeting in ECNU",
                "Shanghai", R.drawable.sports_meeting, "https://www.tripadvisor.com/Attractions-g308272-Activities-c40-Shanghai.html"));
        placeRcmdItemList.add(new PlaceRcmdItem("Liwa Restaurant",
                "Shanghai", R.drawable.liwa_restaurant_1, "https://impactnottingham.com/2013/10/shanghai-nights-and-smuggling-days/"));
        placeRcmdItemList.add(new PlaceRcmdItem("Autumn in ECNU",
                "Shanghai", R.drawable.hsd_0, "http://www.timeoutshanghai.com/venue/shops__services-markets/726/south-bund-fabric-market.html"));
        placeRcmdItemList.add(new PlaceRcmdItem("A day to the ancient water town",
                "Shanghai", R.drawable.ancient_watertown, "https://irene-travelogue.com/2011/12/22/a-day-to-the-ancient-watertown/"));
        recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlaceRcmdAdapter(placeRcmdItemList);
        recyclerView.setAdapter(adapter);
        currentCityText = view.findViewById(R.id.current_city_text);
        getCurrentCity();

        return view;
    }

    private void getCurrentCity() {
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                StringBuilder currentPosition = new StringBuilder();
                //Log.e(TAG,"latitude:"+bdLocation.getLatitude()+" longtitude: "+bdLocation.getLongitude()+" "
                // +bdLocation.getProvince()+" "+bdLocation.getCity()+" "+bdLocation.getDistrict()+" "
                // +bdLocation.getStreet());
                currentPosition.append(bdLocation.getCity());
                currentCityText.setText(currentPosition);
            }
        });
        SDKInitializer.initialize(getContext().getApplicationContext());
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
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
        option.setScanSpan(300000); //access current city per 5 mins
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            //finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                break;
            default:
        }
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }

    }

    /*public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }*/
}
