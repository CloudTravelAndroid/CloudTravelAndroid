package com.cloudtravel.cloudtravelandroid.main.service;

import com.cloudtravel.cloudtravelandroid.main.dto.BaseResponse;
import com.cloudtravel.cloudtravelandroid.main.dto.MomentsCommentDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.MomentsDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.ProvinceDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.ScheduleDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.SimpleUniversityDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.UniversityDTO;
import com.cloudtravel.cloudtravelandroid.main.dto.UserDTO;
import com.cloudtravel.cloudtravelandroid.main.form.MomentsCommentForm;
import com.cloudtravel.cloudtravelandroid.main.form.ScheduleForm;
import com.cloudtravel.cloudtravelandroid.main.form.ScheduleUpdateForm;
import com.cloudtravel.cloudtravelandroid.main.form.UserSignInForm;
import com.cloudtravel.cloudtravelandroid.main.form.UserSignUpForm;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CloudTravelAPI {

    @POST("users/actions/signUp")
    Call<BaseResponse<String>> signUp(@Body UserSignUpForm signUpForm);

    @POST("users/actions/signIn")
    Call<BaseResponse<String>> signIn(@Body UserSignInForm signInForm);

    @GET("users/actions/logOut")
    Call<BaseResponse> logOut();

    @GET("users/info")
    Call<BaseResponse<UserDTO>> getUserInfo();

    @GET("provinces")
    Call<BaseResponse<List<ProvinceDTO>>> getProvinces();

    @POST("provinces/{ID}")
    Call<BaseResponse<List<SimpleUniversityDTO>>> getSimpleUniversityByProvinceID(@Path("ID") Integer ID);

    @POST("provinces/universities/{ID}")
    Call<BaseResponse<UniversityDTO>> getUniversityByUniversityID(@Path("ID") Integer ID);

    @POST("universities/search")
    Call<BaseResponse<SimpleUniversityDTO>> getSimpleUniversityByName(String name);

    @GET("schedules/current")
    Call<BaseResponse<List<ScheduleDTO>>> getCurrentSchedule();

    @POST("schedules/time")
    @FormUrlEncoded
    Call<BaseResponse<List<ScheduleDTO>>> getSchedule(@Field("time") String time);

    @POST("schedules")
    Call<BaseResponse> createSchedule(@Body ScheduleForm scheduleForm);

    @POST("schedules/delete")
    @FormUrlEncoded
    Call<BaseResponse> deleteSchedule(@Field("scheduleID") Integer scheduleID);

    @POST("schedules/update")
    Call<BaseResponse> updateSchedule(@Body ScheduleUpdateForm scheduleUpdateForm);

    @POST("moments/latest")
    @FormUrlEncoded
    Call<BaseResponse<List<MomentsDTO>>> getLatestMoments(@Field("size") Integer size);

    @POST("moments/me")
    @FormUrlEncoded
    Call<BaseResponse<List<MomentsDTO>>> getLatestUserRelatedMoments(@Field("size") Integer size);

    @Multipart
    @POST("moments")
    Call<BaseResponse> createMoments(@Part("content") String content,
                                     @Part("universityID") Integer universityID,
                                     @Part List<MultipartBody.Part> images);

    @POST("moments/comments")
    @FormUrlEncoded
    Call<BaseResponse<List<MomentsCommentDTO>>> getMomentsCommentsByMomentsID(
            @Field("momentsID") Integer momentsID, @Field("size") Integer size);

    @POST("moments/actions/delete")
    @FormUrlEncoded
    Call<BaseResponse> deleteMoments(@Field("momentsID") Integer momentsID);

    @POST("moments/comments/actions/create")
    Call<BaseResponse> createMomentsComment(@Body MomentsCommentForm commentForm);

}
