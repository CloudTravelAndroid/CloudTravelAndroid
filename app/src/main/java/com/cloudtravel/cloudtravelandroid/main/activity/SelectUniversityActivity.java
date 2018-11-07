package com.cloudtravel.cloudtravelandroid.main.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.dto.ProvinceDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.SimpleUniversityDTO;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectUniversityActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_UNIVERSITY = 1;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private TextView titleText;
    private Button backButton;
    private ProgressBar progressBar;

    private List<String> dataList = new ArrayList<>();
    private List<ProvinceDTO> provinceList = new ArrayList<>();
    private List<SimpleUniversityDTO> universityList = new ArrayList<>();
    private ProvinceDTO selectedProvince;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_university);

        titleText = findViewById(R.id.title_text);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_bar);
        listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(i);
                    queryUniversities();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_PROVINCE) {
                    finish();
                } else if (currentLevel == LEVEL_UNIVERSITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText("中国");
        if (provinceList == null || provinceList.isEmpty()) {
            showProgressBar();
            Call<BaseResponse<List<ProvinceDTO>>> call = CloudTravelService.getInstance().getProvinces();
            call.enqueue(new Callback<BaseResponse<List<ProvinceDTO>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<ProvinceDTO>>> call,
                                       Response<BaseResponse<List<ProvinceDTO>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            List<ProvinceDTO> provinceDTOS = response.body().getObject();
                            if (provinceDTOS != null && !provinceDTOS.isEmpty()) {
                                provinceList = provinceDTOS;
                                dataList.clear();
                                for (ProvinceDTO provinceDTO : provinceList) {
                                    dataList.add(provinceDTO.getName());
                                }
                                adapter.notifyDataSetChanged();
                                listView.setSelection(0);
                                currentLevel = LEVEL_PROVINCE;
                                hideProgressBar();
                            } else {
                                Toast.makeText(SelectUniversityActivity.this, "检索结果为空",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SelectUniversityActivity.this,
                                    response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SelectUniversityActivity.this, "未知错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<ProvinceDTO>>> call, Throwable t) {
                    Toast.makeText(SelectUniversityActivity.this, "请求失败",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dataList.clear();
            for (ProvinceDTO provinceDTO : provinceList) {
                dataList.add(provinceDTO.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
    }

    private void queryUniversities() {
        titleText.setText(selectedProvince.getName());
        showProgressBar();
        Call<BaseResponse<List<SimpleUniversityDTO>>> call = CloudTravelService.getInstance()
                .getSimpleUniversityByProvinceID(selectedProvince.getId());
        call.enqueue(new Callback<BaseResponse<List<SimpleUniversityDTO>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SimpleUniversityDTO>>> call,
                                   Response<BaseResponse<List<SimpleUniversityDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        List<SimpleUniversityDTO> simpleUniversityDTOS = response.body()
                                .getObject();
                        if (simpleUniversityDTOS != null && !simpleUniversityDTOS.isEmpty()) {
                            dataList.clear();
                            universityList = simpleUniversityDTOS;
                            for (SimpleUniversityDTO simpleUniversityDTO : universityList) {
                                dataList.add(simpleUniversityDTO.getName());
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            currentLevel = LEVEL_UNIVERSITY;
                            hideProgressBar();
                        } else {
                            Toast.makeText(SelectUniversityActivity.this, "检索结果为空",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SelectUniversityActivity.this,
                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SelectUniversityActivity.this, "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SimpleUniversityDTO>>> call, Throwable t) {
                Toast.makeText(SelectUniversityActivity.this, "请求失败",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
