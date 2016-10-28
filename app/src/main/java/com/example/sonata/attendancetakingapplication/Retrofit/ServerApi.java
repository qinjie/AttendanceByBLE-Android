package com.example.sonata.attendancetakingapplication.Retrofit;

/**
 * Created by Sonata on 10/26/2016.
 */

import com.example.sonata.attendancetakingapplication.Model.LoginInfo;
import com.example.sonata.attendancetakingapplication.Model.SignupInfo;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerApi {

    @GET("user/logout")
    Call<ResponseBody> logout();

    @POST("student/login")
    Call<ResponseBody> login(@Body LoginInfo param);

    @POST("user/register-device")
    Call<ResponseBody> registerDevice(@Body JsonObject toUp);

    @POST("user/reset-password")
    Call<ResponseBody> resetPassword(@Body JsonObject email);

    @POST("user/signup-student")
    Call<ResponseBody> signup(@Body SignupInfo user);

}
