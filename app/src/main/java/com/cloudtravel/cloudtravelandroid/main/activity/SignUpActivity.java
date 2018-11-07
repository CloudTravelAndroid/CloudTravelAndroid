package com.cloudtravel.cloudtravelandroid.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.main.constant.TokenConstant;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.form.UserSignUpForm;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;
import com.cloudtravel.cloudtravelandroid.main.util.ContextUtil;
import com.lemon.support.util.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends CloudTravelBaseActivity {

    private Button signUpButton;
    private TextView signInTextView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
        View view = findViewById(R.id.sign_up_layout);
        view.getBackground().setAlpha(200);
        signUpButton = findViewById(R.id.sign_up_button);
        signInTextView = findViewById(R.id.sign_in_text_view);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.password_confirm_edit_text);
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                    ToastUtils.show(SignUpActivity.this, "两次密码不一致");
                } else {
                    UserSignUpForm signUpForm = new UserSignUpForm();
                    signUpForm.setName(emailEditText.getText().toString());
                    signUpForm.setPassword(emailEditText.getText().toString());
                    signUp(signUpForm);
                }
            }
        });
    }

    private void signUp(UserSignUpForm signUpForm) {
        Call<BaseResponse<String>> call = CloudTravelService.getInstance().signUp(signUpForm);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call,
                                   Response<BaseResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        String token = response.body().getObject();
                        PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext())
                                .edit().putString(TokenConstant.TOKEN, token).apply();
                        Intent intent = new Intent(SignUpActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ContextUtil.getContext(), response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContextUtil.getContext(), "未知错误", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(ContextUtil.getContext(), "请求失败", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
