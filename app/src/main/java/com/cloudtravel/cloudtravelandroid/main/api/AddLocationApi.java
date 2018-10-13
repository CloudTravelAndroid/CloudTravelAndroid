package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.AddLocationRequest;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddLocationApi {
    @POST("location/addLocation")
    SimpleCall<CloudTravelBaseResponse> doAddLocation(@Body AddLocationRequest request);
}
