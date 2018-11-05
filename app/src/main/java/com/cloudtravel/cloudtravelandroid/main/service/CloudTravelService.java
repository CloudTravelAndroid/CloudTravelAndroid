package com.cloudtravel.cloudtravelandroid.main.service;

import android.preference.PreferenceManager;

import com.cloudtravel.cloudtravelandroid.main.constant.TokenConstant;
import com.cloudtravel.cloudtravelandroid.main.util.ContextUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CloudTravelService {

    private static String BASE_URL = "http://47.100.253.251/cloudtravel/";

    private static CloudTravelAPI cloudTravelAPIInstance = null;

    public static CloudTravelAPI getInstance() {
        Interceptor tokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request tokenRequest;
                String token = PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext())
                        .getString(TokenConstant.TOKEN, null);
                if (token == null) {
                    return chain.proceed(originalRequest);
                }
                tokenRequest = originalRequest.newBuilder().header(TokenConstant.TOKEN, token).build();
                return chain.proceed(tokenRequest);
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(tokenInterceptor)
                .build();
        if (cloudTravelAPIInstance == null) {
            synchronized (CloudTravelAPI.class) {
                if (cloudTravelAPIInstance == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    cloudTravelAPIInstance = retrofit.create(CloudTravelAPI.class);
                }
            }
        }
        return cloudTravelAPIInstance;
    }
}
