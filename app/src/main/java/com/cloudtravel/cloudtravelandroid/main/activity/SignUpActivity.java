package com.cloudtravel.cloudtravelandroid.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudtravel.cloudtravelandroid.R;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseActivity;
import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseCallBack;
import com.cloudtravel.cloudtravelandroid.main.api.SignUpApi;
import com.cloudtravel.cloudtravelandroid.main.request.SignUpRequest;
import com.lemon.support.util.ToastUtils;

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
        View view=findViewById(R.id.sign_up_layout);
        view.getBackground().setAlpha(200);
        signUpButton=findViewById(R.id.sign_up_button);
        signInTextView=findViewById(R.id.sign_in_text_view);
        emailEditText=findViewById(R.id.email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);
        confirmPasswordEditText=findViewById(R.id.password_confirm_edit_text);
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
                    SignUpRequest request = new SignUpRequest();
                    request.setEmail(emailEditText.getText().toString());
                    request.setPassword(passwordEditText.getText().toString());
                    addRequest(getService(SignUpApi.class).doSignUp(request), new CloudTravelBaseCallBack() {
                        @Override
                        public void onSuccess200(Object o) {
                            makeToast("注册成功");
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
