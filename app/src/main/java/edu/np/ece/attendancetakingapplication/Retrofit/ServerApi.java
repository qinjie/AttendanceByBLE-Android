package edu.np.ece.attendancetakingapplication.Retrofit;

/**
 * Created by Sonata on 10/26/2016.
 */

import edu.np.ece.attendancetakingapplication.Model.HistoricalResult;
import edu.np.ece.attendancetakingapplication.Model.LoginInfo;
import edu.np.ece.attendancetakingapplication.Model.LoginResult;
import edu.np.ece.attendancetakingapplication.Model.TimetableResult;

import com.google.gson.JsonObject;

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
    Call<String> takeAttendance(@Body JsonObject obj);

    @POST("timetable/get-status")
    Call<String> checkAttendanceStatus(@Body JsonObject obj);

}
