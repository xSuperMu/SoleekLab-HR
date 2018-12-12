package com.example.moham.soleeklab;

import android.app.Application;

import com.example.moham.soleeklab.Model.Employee;


/**
 * I created this class for a specific purpose which is
 * making a global variable keep its value around the lifecycle of the application
 * regardless which activity is running.
 * <p>
 * I need this class to keep the current Employee session, by that I mean to keep the Employee logged in to the app
 * even when the app gets destroyed.
 * <p>
 * The user's session gets lost only when the Employee sign out from the app.
 */

public class EmployeeApplication extends Application {

    private static final String TAG = EmployeeApplication.class.getSimpleName();
    private static EmployeeApplication mInstance;
    private Employee currentEmployee;

    public EmployeeApplication() {
    }

    public EmployeeApplication(Employee currentEmployee) {

        this.currentEmployee = currentEmployee;
    }

    public static synchronized EmployeeApplication getInstance() {
        return mInstance;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
