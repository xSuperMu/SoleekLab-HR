package com.example.moham.soleeklab.Network;

import android.content.Context;

import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;

import java.util.HashMap;

public class HeaderInjectorImplementation implements HeaderInjector {
    private Context mContext;

    public HeaderInjectorImplementation(Context context) {
        mContext = context;
    }

    @Override
    public HashMap<String, String> getHeaders() {

        String employeeToken = EmployeeSharedPreferences.readEmployeeFromPreferences(mContext).getToken();

        HashMap<String, String> headers = new HashMap<>();
        headers.put(KEY_CONTENT_TYPE_HEADER, APPLICATION_JSON);
        headers.put(KEY_AUTH_HEADER, KEY_Bearer + employeeToken);
        return headers;
    }
}
