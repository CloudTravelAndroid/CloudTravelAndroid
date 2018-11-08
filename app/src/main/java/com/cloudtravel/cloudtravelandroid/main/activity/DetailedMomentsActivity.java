package com.cloudtravel.cloudtravelandroid.main.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedMomentsActivity extends AppCompatActivity {

    public static String ID = "id";
    public static String NAME = "name";
    public static String TIME = "time";
    public static String CONTENT = "content";
    public static String PHOTOS = "photos";
    public static String AVATAR = "avatar";
    public static String USERNAME = "username";

    private TextView nameTextView;
    private TextView timeTextView;
    private CircleImageView avatarImageView;
    private Button deleteButton;
    private Button backButton;
    private TextView contentTextView;
    private BGANinePhotoLayout bgaNinePhotoLayout;

    private Integer momentsID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_moments);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nameTextView = findViewById(R.id.name_text_view);
        timeTextView = findViewById(R.id.time_text_view);
        avatarImageView = findViewById(R.id.avatar_image);
        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (momentsID == -1) {
                    return;
                }
                Call<BaseResponse> call = CloudTravelService.getInstance().deleteMoments(momentsID);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call,
                                           Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 0) {
                                Toast.makeText(DetailedMomentsActivity.this, "删除成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DetailedMomentsActivity.this,
                                        response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DetailedMomentsActivity.this, "未知错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(DetailedMomentsActivity.this, "请求失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        contentTextView = findViewById(R.id.content_text_view);
        bgaNinePhotoLayout = findViewById(R.id.npl_item_moment_photos);

        username = getIntent().getStringExtra(USERNAME);
        momentsID = getIntent().getIntExtra(ID, -1);
        nameTextView.setText(getIntent().getStringExtra(NAME));
        timeTextView.setText(getIntent().getStringExtra(TIME));
        contentTextView.setText(getIntent().getStringExtra(CONTENT));
        bgaNinePhotoLayout.setData(getIntent().getStringArrayListExtra(PHOTOS));

        if (username.equals(nameTextView.getText().toString())) {
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
}
