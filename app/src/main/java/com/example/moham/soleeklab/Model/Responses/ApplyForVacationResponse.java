package com.example.moham.soleeklab.Model.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApplyForVacationResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private ApplyForVacationResponse applyForVacationResponse;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("type")
    @Expose
    private String vacationType;
    @SerializedName("vacation_reason")
    @Expose
    private String vacationReason;
    @SerializedName("total_days")
    @Expose
    private String totalDays;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("id")
    @Expose
    private String id;

    public ApplyForVacationResponse() {
    }

    public ApplyForVacationResponse getApplyForVacationResponse() {
        return applyForVacationResponse;
    }

    public void setApplyForVacationResponse(ApplyForVacationResponse applyForVacationResponse) {
        this.applyForVacationResponse = applyForVacationResponse;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVacationType() {
        return vacationType;
    }

    public void setVacationType(String vacationType) {
        this.vacationType = vacationType;
    }

    public String getVacationReason() {
        return vacationReason;
    }

    public void setVacationReason(String vacationReason) {
        this.vacationReason = vacationReason;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
