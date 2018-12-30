package com.example.moham.soleeklab.Model.Responses;

import com.example.moham.soleeklab.Model.Vacation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacationResponse {

    @SerializedName("data")
    @Expose
    private Vacation mVacation;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;

    public Vacation getmVacation() {
        return mVacation;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
