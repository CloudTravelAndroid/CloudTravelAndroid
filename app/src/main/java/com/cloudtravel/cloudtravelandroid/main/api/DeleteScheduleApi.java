package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.DeleteScheduleRequest;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeleteScheduleApi {
    @POST("/schedule/deleteSchedule")
    SimpleCall<CloudTravelBaseResponse> doDeleteSchedule(@Body DeleteScheduleRequest request);
}
