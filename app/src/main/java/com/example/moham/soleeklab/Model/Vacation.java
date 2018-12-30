package com.example.moham.soleeklab.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Vacation implements Serializable {

    @SerializedName("vacations")
    @Expose
    private List<Vacation> vacation;

    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("type")
    @Expose
    private String vacationType;
    @SerializedName("total_days")
    @Expose
    private String totalDays;
    @SerializedName("vacation_reason")
    @Expose
    private String vacationReason;
    @SerializedName("admin_start_date")
    @Expose
    private String adminStartDate;
    @SerializedName("admin_end_date")
    @Expose
    private String adminEndDate;
    @SerializedName("rejection_reason")
    @Expose
    private String rejectionReason;
    @SerializedName("state")
    @Expose
    private String state;

    public Vacation() {
    }

    public List<Vacation> getVacation() {
        return vacation;
    }

    public int getCount() {
        return count;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getVacationType() {
        return vacationType;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public String getVacationReason() {
        return vacationReason;
    }

    public String getAdminStartDate() {
        return adminStartDate;
    }

    public String getAdminEndDate() {
        return adminEndDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public String getState() {
        return state;
    }
}