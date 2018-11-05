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

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.dto.ProvinceDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.UniversityDTO;

import java.util.ArrayList;
import java.util.List;

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
    private List<UniversityDTO> universityList = new ArrayList<>();
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
        // Todo: get province list

        dataList.clear();
        for (ProvinceDTO provinceDTO : provinceList) {
            dataList.add(provinceDTO.getName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_PROVINCE;
    }

    private void queryUniversities() {
        titleText.setText(selectedProvince.getName());
        // Todo: get university list

        dataList.clear();
        for (UniversityDTO universityDTO : universityList) {
            dataList.add(universityDTO.getName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
        currentLevel = LEVEL_UNIVERSITY;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
