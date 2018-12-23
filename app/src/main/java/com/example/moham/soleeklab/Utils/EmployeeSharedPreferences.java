package com.example.moham.soleeklab.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.moham.soleeklab.EmployeeApplication;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.R;
import com.google.gson.Gson;

import static com.example.moham.soleeklab.Utils.Constants.TAG_EMPLOYEE_SHARED_PREF;

public class EmployeeSharedPreferences {


    public static void SaveEmployeeToPreferences(Context context, Employee employee) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "SaveEmployeeToPreferences() has been instantiated");

        Gson gson = new Gson();
        String empStr = gson.toJson(employee);

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("EMPLOYEE", empStr);
        editor.apply();

        ((EmployeeApplication) context.getApplicationContext()).setCurrentEmployee(employee);
    }

    public static Employee readFromPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "readFromPreferences() has been instantiated");

        Employee employee;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Gson gson = new Gson();
        employee = gson.fromJson(sharedPref.getString("EMPLOYEE", ""), Employee.class);

        if (TextUtils.isEmpty(employee.getToken()))
            return null;

        return employee;
    }

    public static void clearPreferences(Context context) {
        Log.d(TAG_EMPLOYEE_SHARED_PREF, "clearPreferences() has been instantiated");
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
