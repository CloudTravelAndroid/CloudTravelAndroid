package com.cloudtravel.cloudtravelandroid.base;

import android.app.Application;
import com.lemon.support.util.DeviceUtil;

import java.text.SimpleDateFormat;
import java.util.Timer;

/**
 * Created by yerunjie
 */

public class CloudTravelBaseApplication extends Application {
    public static CloudTravelBaseApplication sAppContext;
    public static String IMEI;
    public static String uid;
    public static String pwd = "";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean firstGetHighSpeed = true;
    public static Timer checkVpnStatusTimer;
    public final static boolean debug = true;
    public final static String HIGH_NODE_IP = "High_Ip_Node";
    public final static String URL_ADDRESSES = "Url_Addresses";
    public final static String ROUTE_INIT = "Route_Init";
    public final static int second = 1000;
    public final static int minute = second * 60;
    public final static int hour = minute * 60;
    public final static int day = hour * 24;

    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        doLogin();
    }

    public void doLogin() {

    }

}
