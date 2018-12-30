package com.example.moham.soleeklab;

import android.app.Application;

import com.example.moham.soleeklab.Model.Responses.EmployeeResponse;


/**
 * I created this class for CircleImageView specific purpose which is
 * making CircleImageView global variable keep its value around the lifecycle of the application
 * regardless which activity is running.
 * <p>
 * I need this class to keep the current EmployeeResponse session, by that I mean to keep the EmployeeResponse logged in to the app
 * even when the app gets destroyed.
 * <p>
 * The user's session gets lost only when the EmployeeResponse sign out from the app.
 */

public class EmployeeApplication extends Application {

    private static final String TAG = EmployeeApplication.class.getSimpleName();
    private static EmployeeApplication mInstance;
    private EmployeeResponse currentEmployeeResponse;

    public EmployeeApplication() {
    }

    public EmployeeApplication(EmployeeResponse currentEmployeeResponse) {

        this.currentEmployeeResponse = currentEmployeeResponse;
    }

    public static synchronized EmployeeApplication getInstance() {
        return mInstance;
    }

    public EmployeeResponse getCurrentEmployeeResponse() {
        return currentEmployeeResponse;
    }

    public void setCurrentEmployeeResponse(EmployeeResponse currentEmployeeResponse) {
        this.currentEmployeeResponse = currentEmployeeResponse;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
