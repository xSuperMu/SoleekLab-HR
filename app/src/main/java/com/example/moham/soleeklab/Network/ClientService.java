package com.example.moham.soleeklab.Network;

import com.example.moham.soleeklab.Model.AttendanceSheetResponse;
import com.example.moham.soleeklab.Model.CheckInResponse;
import com.example.moham.soleeklab.Model.Employee;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientService {

    @POST("api/users/login")
    @FormUrlEncoded
    Call<Employee> loginEmployee(@Field("email") String email, @Field("password") String password);

    @POST("api/users/rest_email")
    @FormUrlEncoded
    Call<Employee> sendEmailToResetPassword(@Field("email") String email);

    @POST("api/users/verify_code")
    @FormUrlEncoded
    Call<Employee> verifyRestCode(@Field("email") String email, @Field("code") int code);

    @POST("api/users/reset_password")
    @FormUrlEncoded
    Call<Employee> resetUserPassword(@Field("email") String email, @Field("password") String password, @Field("code") String code);

    @GET("api/members/today_attendance")
    Call<CheckInResponse> getTodayAttendance(@HeaderMap Map<String, String> headers);

    @POST("api/members/checkin")
    Call<CheckInResponse> checkInUser(@HeaderMap Map<String, String> headers);

    @POST("api/members/checkout")
    Call<CheckInResponse> checkOutUser(@HeaderMap Map<String, String> headers);

    @GET("api/members/attendance")
    Call<AttendanceSheetResponse> getUserAttendanceSheet(@HeaderMap Map<String, String> headers, @Query("date") String date);
}
