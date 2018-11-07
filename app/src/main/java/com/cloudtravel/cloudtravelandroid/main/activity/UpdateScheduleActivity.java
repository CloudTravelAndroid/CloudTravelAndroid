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
import com.cloudtravel.cloudtravelandroid.main.form.ScheduleUpdateForm;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;
import com.lemon.support.util.DateUtil;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateScheduleActivity extends CloudTravelBaseActivity {

    private Button dateButton;
    private EditText memoEditText;
    private FloatingActionButton backButton;
    private FloatingActionButton checkButton;
    private TimeSelector timeSelector;
    private TextView placeNameText;
    private TextView dateText;
    private int scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
        dateText = findViewById(R.id.date_text_view);
        dateButton = findViewById(R.id.date_select_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelector = new TimeSelector(UpdateScheduleActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        dateText.setText(time);
                    }
                }, "2018-1-1 00:00", "2050-12-31 23:59");
                timeSelector.setIsLoop(false);
                timeSelector.show();
            }
        });
        placeNameText = findViewById(R.id.place_name);
        memoEditText = findViewById(R.id.schedule_memo);
        backButton = findViewById(R.id.back_button);
        checkButton = findViewById(R.id.check_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSchedule();
                finish();
            }
        });
        placeNameText.setText(getIntent().getStringExtra("name"));
        memoEditText.setText(getIntent().getStringExtra("memo"));
        Date date;
        date = DateUtil.str2Date(getIntent().getStringExtra("time"));
        dateText.setText(DateUtil.date2Str(date, "YYYY-MM-dd HH:mm"));
        scheduleId = getIntent().getIntExtra("id", 0);
    }

    private void updateSchedule() {
        ScheduleUpdateForm updateForm = new ScheduleUpdateForm();
        updateForm.setId(scheduleId);
        updateForm.setMemo(memoEditText.getText().toString());
        String time = dateText.getText().toString();
        time += ":00";
        updateForm.setTime(time);
        Call<BaseResponse> call = CloudTravelService.getInstance().updateSchedule(updateForm);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        Toast.makeText(UpdateScheduleActivity.this, "更新成功",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateScheduleActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateScheduleActivity.this, "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(UpdateScheduleActivity.this, "请求失败",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
