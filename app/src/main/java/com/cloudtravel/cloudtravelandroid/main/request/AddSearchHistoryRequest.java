package com.cloudtravel.cloudtravelandroid.main.request;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseRequest;

/**
 * Created by Brandon on 2018/3/27.
 */

public class AddSearchHistoryRequest extends CloudTravelBaseRequest {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
