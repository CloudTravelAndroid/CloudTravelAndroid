package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.cloudtravel.cloudtravelandroid.main.request.UpdateScheduleRequest;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Brandon on 2018/3/27.
 */

public interface UpdateScheduleApi {
    @POST("/schedule/updateSchedule")
    SimpleCall<CloudTravelBaseResponse> doUpdateSchedule(@Body UpdateScheduleRequest request);
}
