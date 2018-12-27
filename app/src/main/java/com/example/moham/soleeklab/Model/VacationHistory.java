package com.example.moham.soleeklab.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VacationHistory implements Serializable {
    @SerializedName("data")
    @Expose
    private List<VacationHistory> vacationHistoryList;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;
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
}

