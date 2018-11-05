package com.cloudtravel.cloudtravelandroid.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.main.request.SignInRequest;

/**
 * Created by KingJ on 2018/10/9.
 */

public class MomentsActivity extends CloudTravelBaseActivity {

    private TextView momentText;
    private FloatingActionButton postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_moments);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
        postButton = findViewById(R.id.post_moment_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInRequest request = new SignInRequest();
            }
        });
        momentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MomentsActivity.this, MomentsDetailedActivity.class);
                startActivity(intent);
            }
        });
    }

}