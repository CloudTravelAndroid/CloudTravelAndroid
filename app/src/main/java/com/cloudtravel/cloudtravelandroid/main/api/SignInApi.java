package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.SignInRequest;
import com.lemon.support.request.SimpleCall;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInApi {
    @POST("user/signIn")
    SimpleCall<CloudTravelBaseResponse> doSignIn(@Body SignInRequest request);
}
