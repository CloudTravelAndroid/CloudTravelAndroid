package com.cloudtravel.cloudtravelandroid.base;

/**
 * Created by yerunjie on 2018/1/13
 *
 * @author yerunjie
 */
public class CloudTravelBaseResponse<T> {
    public String returnCode;
    public String returnMsg;
    public T obj;

    public boolean isOk() {
        return returnCode.equals("0");
    }

    public boolean isExpired() {
        // TODO: 2018/1/20 还不知道返回码是什么
        return returnCode.equals("0");
    }
}
