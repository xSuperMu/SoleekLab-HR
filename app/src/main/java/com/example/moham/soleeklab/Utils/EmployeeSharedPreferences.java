package com.example.moham.soleeklab.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.moham.soleeklab.EmployeeApplication;
import com.example.moham.soleeklab.Model.Data;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Model.User;
import com.example.moham.soleeklab.R;

import static com.example.moham.soleeklab.Utils.Constants.TAG_EMPLOYEE_SHARED_PREF;

public class EmployeeSharedPreferences {

    public static void SaveEmployeeToPreferences(Context context, Employee employee) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "SaveEmployeeToPreferences() has been instantiated");

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("EMPLOYEE_TOKEN", employee.getData().getToken());

        editor.putString("EMPLOYEE_EMAIL", employee.getData().getUser().getEmail());
        editor.putString("EMPLOYEE_NAME", employee.getData().getUser().getName());
        editor.putInt("EMPLOYEE_ID", employee.getData().getUser().getId());
        editor.putString("EMPLOYEE_JOB_TITLE", employee.getData().getUser().getJobTitle());
        editor.putString("EMPLOYEE_PROFILE_PIC", employee.getData().getUser().getProfilePic());
        editor.putString("EMPLOYEE_CREATED_AT", employee.getData().getUser().getCreatedAt());
        editor.apply();

        ((EmployeeApplication) context.getApplicationContext()).setCurrentEmployee(employee);
    }

    public static Employee readFromPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "readFromPreferences() has been instantiated");

        Employee employee = new Employee();
        Data employeeData = new Data();
        User userEmployee = new User();
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        employeeData.setToken(sharedPref.getString("EMPLOYEE_TOKEN", ""));
        userEmployee.setId(sharedPref.getInt("EMPLOYEE_ID", 0));
        userEmployee.setEmail(sharedPref.getString("EMPLOYEE_EMAIL", ""));
        userEmployee.setName(sharedPref.getString("EMPLOYEE_NAME", ""));
        userEmployee.setJobTitle(sharedPref.getString("EMPLOYEE_JOB_TITLE", ""));
        userEmployee.setProfilePic(sharedPref.getString("EMPLOYEE_PROFILE_PIC", ""));
        userEmployee.setCreatedAt(sharedPref.getString("EMPLOYEE_CREATED_AT", ""));

        employeeData.setUser(userEmployee);
        employee.setData(employeeData);

        if (TextUtils.isEmpty(employeeData.getToken()))
            return null;

        return employee;
    }


    public static void clearPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "clearPreferences() has been instantiated");

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_TOKEN" + sharedPref.getString("EMPLOYEE_TOKEN", ""));
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_ID" + sharedPref.getInt("EMPLOYEE_ID", 0));
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_EMAIL" + sharedPref.getString("EMPLOYEE_EMAIL", ""));
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_NAME" + sharedPref.getString("EMPLOYEE_NAME", ""));
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_JOB_TITLE" + sharedPref.getString("EMPLOYEE_JOB_TITLE", ""));
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_PROFILE_PIC" + sharedPref.getString("EMPLOYEE_PROFILE_PIC", ""));
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "EMPLOYEE_CREATED_AT" + sharedPref.getString("EMPLOYEE_CREATED_AT", ""));
    }
}
