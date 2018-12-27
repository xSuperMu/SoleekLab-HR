package com.example.moham.soleeklab.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AttendanceSheetResponse implements Serializable {

    @SerializedName("data")
    @Expose
    private List<AttendanceSheetResponse> attendanceSheetResponse;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("state")
    @Expose
    private int state;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("verified")
    @Expose
    private Integer verified;
    @SerializedName("admin_checkin")
    @Expose
    private Object adminCheckin;
    @SerializedName("admin_checkout")
    @Expose
    private Object adminCheckout;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private String error;


    public AttendanceSheetResponse() {
    }

    public List<AttendanceSheetResponse> getAttendanceSheetResponse() {
        return attendanceSheetResponse;
    }

    public void setAttendanceSheetResponse(List<AttendanceSheetResponse> attendanceSheetResponse) {
        this.attendanceSheetResponse = attendanceSheetResponse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Object getAdminCheckin() {
        return adminCheckin;
    }

    public void setAdminCheckin(Object adminCheckin) {
        this.adminCheckin = adminCheckin;
    }

    public Object getAdminCheckout() {
        return adminCheckout;
    }

    public void setAdminCheckout(Object adminCheckout) {
        this.adminCheckout = adminCheckout;
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
}
