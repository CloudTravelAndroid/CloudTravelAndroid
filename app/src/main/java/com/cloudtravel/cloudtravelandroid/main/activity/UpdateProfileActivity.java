package com.cloudtravel.cloudtravelandroid.main.activity;

import android.os.Bundle;
import android.view.View;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;

/**
 * Created by KingJ on 2018/11/8.
 */

public class UpdateProfileActivity  extends CloudTravelBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
    }
}
