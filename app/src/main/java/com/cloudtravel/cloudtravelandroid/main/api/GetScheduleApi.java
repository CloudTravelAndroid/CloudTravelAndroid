package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.GetScheduleRequest;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Brandon on 2018/3/26.
 */

public interface GetScheduleApi {
    @POST("/schedule/getSchedule")
    SimpleCall<CloudTravelBaseResponse> doGetSchedule(@Body GetScheduleRequest request);
}
