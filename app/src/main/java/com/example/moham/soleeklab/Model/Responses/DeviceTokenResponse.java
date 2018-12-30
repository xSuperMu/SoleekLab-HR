package com.example.moham.soleeklab.Model.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceTokenResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private DeviceTokenResponse deviceTokenResponse;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private int id;

    public DeviceTokenResponse() {
    }

    public DeviceTokenResponse getDeviceTokenResponse() {
        return deviceTokenResponse;
    }

    public void setDeviceTokenResponse(DeviceTokenResponse deviceTokenResponse) {
        this.deviceTokenResponse = deviceTokenResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
