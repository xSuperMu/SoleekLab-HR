package com.example.moham.soleeklab.Network;

import com.example.moham.soleeklab.Model.Employee;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
}
