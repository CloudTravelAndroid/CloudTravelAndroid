package com.cloudtravel.cloudtravelandroid.main.util;

import com.cloudtravel.cloudtravelandroid.base.CloudTravelBaseResponse;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil {
    public static <T> T getEntity(Object o, Type type) {
        T result = null;
        if (o instanceof CloudTravelBaseResponse) {
            Gson gson = new Gson();
            String tmpstr = gson.toJson(((CloudTravelBaseResponse) o).obj);
            result = gson.fromJson(tmpstr, type);
        }
        return result;
    }
}