package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.AddScheduleRequest;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Brandon on 2018/3/27.
 */

public interface AddScheduleApi {
    @POST("/schedule/addSchedule")
    SimpleCall<CloudTravelBaseResponse> doAddSchedule(@Body AddScheduleRequest request);
}
