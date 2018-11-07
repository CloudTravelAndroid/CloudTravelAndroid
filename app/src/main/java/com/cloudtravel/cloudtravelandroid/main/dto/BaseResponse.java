package com.cloudtravel.cloudtravelandroid.main.dto;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private Integer status;
    private String message;
    private T object;

    public BaseResponse() {
    }
}
