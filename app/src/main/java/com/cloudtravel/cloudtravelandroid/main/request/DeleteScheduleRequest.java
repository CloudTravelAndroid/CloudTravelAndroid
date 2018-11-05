package com.cloudtravel.cloudtravelandroid.main.request;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseRequest;

public class DeleteScheduleRequest extends CloudTravelBaseRequest {
    private int scheduleId;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
