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
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.form.ScheduleForm;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;

import org.feezu.liuli.timeselector.TimeSelector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends CloudTravelBaseActivity {

    private static final String TAG = "AddScheduleActivity";

    private FloatingActionButton backButton;
    private TextView placeNameEditText;
    private Double latitude;
    private Double longitude;
    private EditText memoEditText;
    private FloatingActionButton checkButton;
    private String placeAddress;
    private String placeName;
    private String placeUID;
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
        dateText = findViewById(R.id.date_text_view);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        placeNameEditText = findViewById(R.id.place_name);
        memoEditText = findViewById(R.id.schedule_memo);
        checkButton = findViewById(R.id.check_button);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSchedule();
                finish();
            }
        });
        placeName = getIntent().getStringExtra("name");
        if (placeName != null) {
            placeNameEditText.setText(placeName);
            placeNameEditText.setEnabled(false);
            latitude = getIntent().getDoubleExtra("lat", 0.00);
            longitude = getIntent().getDoubleExtra("lng", 0.00);
            placeAddress = getIntent().getStringExtra("address");
            placeUID = getIntent().getStringExtra("uid");
        }
        dateButton = findViewById(R.id.date_select_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector = new TimeSelector(AddScheduleActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        dateText.setText(time);
                    }
                }, "2018-1-1 00:00", "2050-12-31 23:59");
                timeSelector.setIsLoop(false);
                timeSelector.show();
            }
        });
    }

    private void createSchedule() {
        ScheduleForm scheduleForm = new ScheduleForm();
        scheduleForm.setName(placeName);
        scheduleForm.setAddress(placeAddress);
        scheduleForm.setLatitude(latitude.toString());
        scheduleForm.setLongitude(longitude.toString());
        scheduleForm.setUid(placeUID);
        scheduleForm.setMemo(memoEditText.getText().toString());
        String time = dateText.getText().toString();
        time += ":00";
        scheduleForm.setTime(time);
        Call<BaseResponse> call = CloudTravelService.getInstance().createSchedule(scheduleForm);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(AddScheduleActivity.this, "创建成功",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddScheduleActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddScheduleActivity.this, "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(AddScheduleActivity.this, "请求失败", Toast.LENGTH_SHORT)
                        .show();
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
