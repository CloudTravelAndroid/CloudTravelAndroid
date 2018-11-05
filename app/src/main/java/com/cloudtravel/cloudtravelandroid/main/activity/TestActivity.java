package com.cloudtravel.cloudtravelandroid.main.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseCallBack;
import com.cloudtravel.cloudtravelandroid.main.api.AddScheduleApi;
import com.cloudtravel.cloudtravelandroid.main.api.GetScheduleApi;
import com.cloudtravel.cloudtravelandroid.main.api.UpdateScheduleApi;
import com.cloudtravel.cloudtravelandroid.main.api.UserInfoApi;
import com.cloudtravel.cloudtravelandroid.main.dto.Schedule;
import com.cloudtravel.cloudtravelandroid.main.dto.User;
import com.cloudtravel.cloudtravelandroid.main.request.AddScheduleRequest;
import com.cloudtravel.cloudtravelandroid.main.request.GetScheduleRequest;
import com.cloudtravel.cloudtravelandroid.main.request.UpdateScheduleRequest;
import com.cloudtravel.cloudtravelandroid.main.util.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.lemon.support.util.DateUtil;

import java.util.Date;
import java.util.List;

public class TestActivity extends CloudTravelBaseActivity {

    private static final String TAG = "TestActivity";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //userInfoTest();
        //scheduleTest();
    }

    private void userInfoTest() {

        textView = findViewById(R.id.test_text);
        addRequest(getService(UserInfoApi.class).doGetUserInfo(), new CloudTravelBaseCallBack() {
            @Override
            public void onSuccess200(Object o) {
                User user = GsonUtil.getEntity(o, new com.google.common.reflect.TypeToken<User>() {
                }.getType());
                textView.setText(user.getUserName());
            }
        });
    }

    private void scheduleTest() {

        AddScheduleRequest addScheduleRequest = new AddScheduleRequest();
        addScheduleRequest.setDate(DateUtil.date2Str(new Date()));
        addScheduleRequest.setLocationId(123);
        addScheduleRequest.setLocationName("华师大");
        addScheduleRequest.setPriority(1);
        addScheduleRequest.setRemark("");
        addRequest(getService(AddScheduleApi.class).doAddSchedule(addScheduleRequest), new CloudTravelBaseCallBack() {
            @Override
            public void onSuccess200(Object o) {

            }
        });

        GetScheduleRequest getScheduleRequest = new GetScheduleRequest();
        getScheduleRequest.setDate(DateUtil.date2Str(new Date()));
        addRequest(getService(GetScheduleApi.class).doGetSchedule(getScheduleRequest), new CloudTravelBaseCallBack() {
            @Override
            public void onSuccess200(Object o) {
                List<Schedule> scheduleList = GsonUtil.getEntity(o, new TypeToken<List<Schedule>>() {
                }.getType());
                for (Schedule schedule : scheduleList) {
                    UpdateScheduleRequest updateScheduleRequest = new UpdateScheduleRequest();
                    updateScheduleRequest.setDate(DateUtil.date2Str(new Date()));
                    updateScheduleRequest.setScheduleId(schedule.getScheduleId());
                    updateScheduleRequest.setPriority(2);
                    updateScheduleRequest.setRemark("hello!");
                    addRequest(getService(UpdateScheduleApi.class).doUpdateSchedule(updateScheduleRequest), new CloudTravelBaseCallBack() {
                        @Override
                        public void onSuccess200(Object o) {

                        }
                    });
                }
            }
        });

    }
}
