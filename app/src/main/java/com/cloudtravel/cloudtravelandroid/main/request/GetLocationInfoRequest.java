package com.cloudtravel.cloudtravelandroid.main.request;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseRequest;

/**
 * Created by Brandon on 2018/3/27.
 */

public class GetLocationInfoRequest extends CloudTravelBaseRequest {
    private Integer locationId;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
}
