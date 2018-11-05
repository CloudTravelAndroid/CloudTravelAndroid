package com.cloudtravel.cloudtravelandroid.main.api;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.lemon.support.request.SimpleCall;

import retrofit2.http.POST;

/**
 * Created by Brandon on 2018/3/27.
 */

public interface GetSearchHistory {
    @POST("/SearchHistory/getSearchHistory")
    SimpleCall<CloudTravelBaseResponse> doGetSearchHistory();
}
