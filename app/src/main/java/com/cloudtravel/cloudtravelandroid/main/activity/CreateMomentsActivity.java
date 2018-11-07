package com.cloudtravel.cloudtravelandroid.main.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

public class CreateMomentsActivity extends AppCompatActivity implements
        BGASortableNinePhotoLayout.Delegate {

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;

    private static final int REQUEST_CODE_PREVIEW_PHOTO = 2;

    private BGASortableNinePhotoLayout ninePhotoLayout;
    private Button cancelButton;
    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_moments);

        ninePhotoLayout = findViewById(R.id.add_photos);
        cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postButton = findViewById(R.id.button_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ninePhotoLayout.setDelegate(this);
        ninePhotoLayout.setMaxItemCount(9);

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(CreateMomentsActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(CreateMomentsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CreateMomentsActivity.this,
                    permissions, 1);
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout,
                                        View view, int position, ArrayList<String> models) {
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(),
                "BGAPhotoPickerTakePhoto");
        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir)
                .maxChooseCount(ninePhotoLayout.getMaxItemCount() - ninePhotoLayout.getItemCount())
                .selectedPhotos(null)
                .pauseOnScroll(false)
                .build();
        startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_PHOTO);
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout,
                                           View view, int position, String model,
                                           ArrayList<String> models) {
        ninePhotoLayout.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout,
                                     View view, int position, String model,
                                     ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity
                .IntentBuilder(this)
                .previewPhotos(models)
                .selectedPhotos(models)
                .maxChooseCount(ninePhotoLayout.getMaxItemCount())
                .currentPosition(position)
                .isFromTakePhoto(false)
                .build();
        startActivityForResult(photoPickerPreviewIntent, REQUEST_CODE_PREVIEW_PHOTO);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout,
                                         int fromPosition, int toPosition,
                                         ArrayList<String> models) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_PHOTO:
                    ninePhotoLayout.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
                    break;
                case REQUEST_CODE_PREVIEW_PHOTO:
                    ninePhotoLayout.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
