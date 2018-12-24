package com.example.moham.soleeklab.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.moham.soleeklab.EmployeeApplication;
import com.example.moham.soleeklab.Model.CheckInResponse;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.R;
import com.google.gson.Gson;

import static com.example.moham.soleeklab.Utils.Constants.STR_PREF_CHECK_IN;
import static com.example.moham.soleeklab.Utils.Constants.STR_PREF_EMPLOYEE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_EMPLOYEE_SHARED_PREF;

public class EmployeeSharedPreferences {

    public static void SaveEmployeeToPreferences(Context context, Employee employee) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "SaveEmployeeToPreferences() has been instantiated");

        Gson gson = new Gson();
        String empStr = gson.toJson(employee);

        Log.d(TAG_EMPLOYEE_SHARED_PREF, "checkInResponseStr --> " + empStr);

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(STR_PREF_EMPLOYEE, empStr);
        editor.apply();
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "Employee object has been saved successfully");

        ((EmployeeApplication) context.getApplicationContext()).setCurrentEmployee(employee);
    }

    public static Employee readEmployeeFromPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "readEmployeeFromPreferences() has been instantiated");

        Employee employee;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        try {
            Gson gson = new Gson();
            String empStr = sharedPref.getString(STR_PREF_EMPLOYEE, "");
            Log.d(TAG_EMPLOYEE_SHARED_PREF, "empStr --> " + empStr);
            employee = gson.fromJson(empStr, Employee.class);
            Log.d(TAG_EMPLOYEE_SHARED_PREF, "Employee toString(): " + employee.getUser().toString());
            Log.d(TAG_EMPLOYEE_SHARED_PREF, "Employee.Token --> " + employee.getToken());
        } catch (NullPointerException e) {
            return null;
        }

        if (TextUtils.isEmpty(employee.getToken())) {
            Log.d(TAG_EMPLOYEE_SHARED_PREF, "Returning null");
            return null;
        }

        return employee;
    }

    public static void SaveCheckInResponseToPreferences(Context context, CheckInResponse checkInResponse) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "SaveCheckInResponseToPreferences() has been instantiated");

        Gson gson = new Gson();
        String checkInResponseStr = gson.toJson(checkInResponse);

        Log.d(TAG_EMPLOYEE_SHARED_PREF, "checkInResponseStr --> " + checkInResponseStr);

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(STR_PREF_CHECK_IN, checkInResponseStr);
        editor.apply();
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "CheckInResponse object has been saved successfully");
    }

    public static CheckInResponse readCheckInResponseFromPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "readCheckInResponseFromPreferences() has been instantiated");

        CheckInResponse checkInResponse;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String checkInResponseStr = sharedPref.getString(STR_PREF_CHECK_IN, "");
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "checkInResponseStr --> " + checkInResponseStr);
        checkInResponse = gson.fromJson(checkInResponseStr, CheckInResponse.class);

        Log.d(TAG_EMPLOYEE_SHARED_PREF, "checkInResponse toString(): " + checkInResponse);
        return checkInResponse.getCheckInResponse();
    }

    public static void clearPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "clearPreferences() has been instantiated");
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
