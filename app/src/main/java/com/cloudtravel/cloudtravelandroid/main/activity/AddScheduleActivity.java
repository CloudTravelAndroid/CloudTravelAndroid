package com.cloudtravel.cloudtravelandroid.main.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseCallBack;
import com.cloudtravel.cloudtravelandroid.main.api.AddLocationApi;
import com.cloudtravel.cloudtravelandroid.main.api.AddScheduleApi;
import com.cloudtravel.cloudtravelandroid.main.request.AddLocationRequest;
import com.cloudtravel.cloudtravelandroid.main.request.AddScheduleRequest;
import com.cloudtravel.cloudtravelandroid.main.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import org.feezu.liuli.timeselector.TimeSelector;

public class AddScheduleActivity extends CloudTravelBaseActivity {

    private static final String TAG = "AddScheduleActivity";

    private FloatingActionButton backButton;
    private TextView placeNameEditText;
    private Double latitude;
    private Double longitude;
    private EditText memoEditText;
    private FloatingActionButton checkButton;
    private int locationId;
    private String placeAddress;
    private String placeName;
    private Button dateButton;
    private TimeSelector timeSelector;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
        dateText=findViewById(R.id.date_text_view);
        backButton =findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        placeNameEditText=findViewById(R.id.place_name);
        memoEditText=findViewById(R.id.schedule_memo);
        checkButton =findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSchedule();
                finish();
            }
        });
        placeName=getIntent().getStringExtra("name");
        if (placeName != null) {
            placeNameEditText.setText(placeName);
            placeNameEditText.setEnabled(false);
            latitude=getIntent().getDoubleExtra("lat",0.00);
            longitude=getIntent().getDoubleExtra("lng",0.00);
            placeAddress=getIntent().getStringExtra("address");
        }
        dateButton =findViewById(R.id.date_select_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector=new TimeSelector(AddScheduleActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        dateText.setText(time);
                    }
                },"2018-1-1 00:00", "2050-12-31 23:59");
                timeSelector.setIsLoop(false);
                timeSelector.show();
            }
        });
    }

    private void saveSchedule() {
        AddLocationRequest addLocationRequest=new AddLocationRequest();
        addLocationRequest.setAddress(placeAddress);
        addLocationRequest.setLatitude(latitude);
        addLocationRequest.setLongitude(longitude);
        addLocationRequest.setName(placeName);
        addRequest(getService(AddLocationApi.class).doAddLocation(addLocationRequest), new CloudTravelBaseCallBack() {
            @Override
            public void onSuccess200(Object o) {
                locationId= GsonUtil.getEntity(o,new TypeToken<Integer>(){}.getType());
                AddScheduleRequest addScheduleRequest=new AddScheduleRequest();
                addScheduleRequest.setRemark(memoEditText.getText().toString());
                addScheduleRequest.setPriority(1);
                addScheduleRequest.setLocationName(placeName);
                StringBuilder date=new StringBuilder(dateText.getText().toString());
                date.append(":00");
                addScheduleRequest.setDate(date.toString());
                addScheduleRequest.setLatitude(latitude);
                addScheduleRequest.setLongitude(longitude);
                addScheduleRequest.setLocationId(locationId);
                addRequest(getService(AddScheduleApi.class).doAddSchedule(addScheduleRequest), new CloudTravelBaseCallBack() {
                    @Override
                    public void onSuccess200(Object o) {
                        Toast.makeText(AddScheduleActivity.this,"Adding Schedule Succeeded!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeSelector != null) {
            timeSelector.dismiss();
        }
    }
}
