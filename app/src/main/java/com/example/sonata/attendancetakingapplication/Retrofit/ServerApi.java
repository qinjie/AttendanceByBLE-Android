package com.example.sonata.attendancetakingapplication.Retrofit;

/**
 * Created by Sonata on 10/26/2016.
 */

import com.example.sonata.attendancetakingapplication.Model.HistoricalResult;
import com.example.sonata.attendancetakingapplication.Model.Lesson;
import com.example.sonata.attendancetakingapplication.Model.LoginInfo;
import com.example.sonata.attendancetakingapplication.Model.LoginResult;
import com.example.sonata.attendancetakingapplication.Model.TimetableResult;
import com.example.sonata.attendancetakingapplication.Model.Venue;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerApi {

    @GET("attendance/day")
    Call<ResponseBody> getTimetableOneDay(@Query("recorded_date") String date);

    @GET("timetable")
    Call<List<TimetableResult>> getTimetableCurrentWeek(@Query("expand") String expand);

    @GET("student/history")
    Call<List<HistoricalResult>> getHistoricalReports();

    @GET("user/logout")
    Call<ResponseBody> logout();

    @POST("user/change-password")
    Call<ResponseBody> changePassword(@Body JsonObject toUp);

    @POST("student/login")
    Call<LoginResult> login(@Body LoginInfo param);

    @POST("user/register-device")
    Call<ResponseBody> registerDevice(@Body JsonObject toUp);

    @POST("user/reset-password")
    Call<ResponseBody> resetPassword(@Body JsonObject email);

    @POST("test/create")
    Call<ResponseBody> pushStudentArrayList(@Body JsonObject toUp);

    @POST("beacon-attendance-student/student-attendance")
    Call<ResponseBody> takeAttendance(@Body JsonObject obj);

}
