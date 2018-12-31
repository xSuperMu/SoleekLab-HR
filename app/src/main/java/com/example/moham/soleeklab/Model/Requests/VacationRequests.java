package com.example.moham.soleeklab.Model.Requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacationRequests {

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("for")
    @Expose
    private int period;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("date")
    @Expose
    private String date;


    public VacationRequests(String date) {
        this.date = date;
    }

    public VacationRequests(String startDate, int period, String reason, String type) {
        this.startDate = startDate;
        this.period = period;
        this.reason = reason;
        this.type = type;
    }
}
