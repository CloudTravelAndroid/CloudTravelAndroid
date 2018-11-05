package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.POST;

public interface UserInfoApi {
    @POST("user/getUserInfo")
    SimpleCall<CloudTravelBaseResponse> doGetUserInfo();
}
