package com.cloudtravel.cloudtravelandroid.main;

import android.app.Application;

import com.cloudtravel.cloudtravelandroid.main.util.ContextUtil;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.initial(this);
    }
}
