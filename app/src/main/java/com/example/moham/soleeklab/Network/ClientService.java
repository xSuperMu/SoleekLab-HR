package com.example.moham.soleeklab.Network;

import com.example.moham.soleeklab.Model.Employee;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
    @FormUrlEncoded
    Call<Employee> getTodayAttendance(@Field("token") String token);

    @POST("api/members/checkin")
    @FormUrlEncoded
    Call<Employee> checkInUser(@Field("token") String token);

    @POST("api/members/checkout")
    @FormUrlEncoded
    Call<Employee> checkOutUser(@Field("token") String token);

    @GET("api/members/attendance")
    @FormUrlEncoded
    Call<Employee> getUserAttendanceSheet(@Field("token") String token, @Field("date") String date);
}
