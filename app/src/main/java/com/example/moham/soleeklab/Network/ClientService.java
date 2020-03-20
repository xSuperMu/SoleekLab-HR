package com.example.moham.soleeklab.Network;

import com.example.moham.soleeklab.Model.Requests.VacationRequests;
import com.example.moham.soleeklab.Model.Responses.ApplyForVacationResponse;
import com.example.moham.soleeklab.Model.Responses.AttendanceSheetResponse;
import com.example.moham.soleeklab.Model.Responses.CheckInResponse;
import com.example.moham.soleeklab.Model.Responses.EmployeeResponse;
import com.example.moham.soleeklab.Model.Responses.VacationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientService {

    @POST("api/users/login")
    @FormUrlEncoded
    Call<EmployeeResponse> loginEmployee(@Field("email") String email, @Field("password") String password);

    @POST("api/users/rest_email")
    @FormUrlEncoded
    Call<EmployeeResponse> sendEmailToResetPassword(@Field("email") String email);

    @POST("api/users/verify_code")
    @FormUrlEncoded
    Call<EmployeeResponse> verifyRestCode(@Field("email") String email, @Field("code") int code);

    @POST("api/users/reset_password")
    @FormUrlEncoded
    Call<EmployeeResponse> resetUserPassword(@Field("email") String email, @Field("password") String password, @Field("code") String code);

    @GET("api/members/today_attendance")
    Call<CheckInResponse> getTodayAttendance(@HeaderMap Map<String, String> headers);

    @POST("api/members/checkin")
    Call<CheckInResponse> checkInUser(@HeaderMap Map<String, String> headers);

    @POST("api/members/checkout")
    Call<CheckInResponse> checkOutUser(@HeaderMap Map<String, String> headers);

    @GET("api/members/attendance")
    Call<AttendanceSheetResponse> getUserAttendanceSheet(@HeaderMap Map<String, String> headers, @Query("date") String date);

//    @POST("api/members/notifications/add_token")
//    Call<DeviceTokenResponse> sendDeviceToken(@HeaderMap Map<String, String> headers, @Field("token") String deviceToken);

    @POST("api/members/vacations/request")
    Call<ApplyForVacationResponse> requestVacation(@HeaderMap Map<String, String> headers, @Body VacationRequests vacationRequests);

    @GET("api/members/vacations/vacations")
    Call<VacationResponse> getVacationHistoryNormal(@HeaderMap Map<String, String> headers, @Query("page") int page);

    @GET("api/members/vacations/vacations")
    Call<VacationResponse> getVacationHistoryWithDate(@HeaderMap Map<String, String> headers, @Query("date") String vacationRequests);
}
