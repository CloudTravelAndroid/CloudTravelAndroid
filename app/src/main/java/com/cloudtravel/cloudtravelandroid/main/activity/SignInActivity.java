package com.cloudtravel.cloudtravelandroid.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.main.constant.TokenConstant;
import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.form.UserSignInForm;
import com.cloudtravel.cloudtravelandroid.main.service.CloudTravelService;
import com.cloudtravel.cloudtravelandroid.main.util.ContextUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends CloudTravelBaseActivity {

    private TextView signUpTextView;
    private Button signInButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RelativeLayout signInLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {
        super.hideTitleBar();
        signInLayout = findViewById(R.id.sign_in_layout);
        signUpTextView = findViewById(R.id.sign_up_text_view);
        signInButton = findViewById(R.id.sign_in_button);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        if (!PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext())
                .getString(TokenConstant.TOKEN, "empty").equals("empty")) {
            Intent intent = new Intent(SignInActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(String username, String password) {
        UserSignInForm signInForm = new UserSignInForm();
        signInForm.setName(username);
        signInForm.setPassword(password);
        Call<BaseResponse<String>> call = CloudTravelService.getInstance().signIn(signInForm);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call,
                                   Response<BaseResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        String token = response.body().getObject();
                        PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext())
                                .edit().putString(TokenConstant.TOKEN, token).apply();
                        Intent intent = new Intent(SignInActivity.this,
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
                Toast.makeText(ContextUtil.getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        signInLayout.getBackground().setAlpha(255);
    }
}
