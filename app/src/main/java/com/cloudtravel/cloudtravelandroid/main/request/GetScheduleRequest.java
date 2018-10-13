package com.cloudtravel.cloudtravelandroid.main.request;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseRequest;

import java.util.Date;

/**
 * Created by Brandon on 2018/3/26.
 */

public class GetScheduleRequest extends CloudTravelBaseRequest{
    private String date;
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
