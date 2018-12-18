package com.example.moham.soleeklab.Model;

public class Attendance {

    String day;
    String dayDate;
    String checkinTime;
    String checkoutTime;
    String totaltime;
    String status;

    public Attendance() {
    }

    public Attendance(String day, String dayDate, String checkinTime, String checkoutTime, String totaltime, String status) {
        this.day = day;
        this.dayDate = dayDate;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
        this.totaltime = totaltime;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
