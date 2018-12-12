package com.example.moham.soleeklab.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonErrorModel {
    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("message")
    @Expose
    public String message;

    public CommonErrorModel() {
    }

    public CommonErrorModel(String error, String message) {
        this.error = error;
        this.message = message;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
