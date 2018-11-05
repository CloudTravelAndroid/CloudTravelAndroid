package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.SignUpRequest;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignUpApi {
    @POST("user/signUp")
    SimpleCall<CloudTravelBaseResponse> doSignUp(@Body SignUpRequest request);
}
