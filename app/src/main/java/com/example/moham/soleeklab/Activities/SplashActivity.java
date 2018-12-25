package com.example.moham.soleeklab.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;

import static com.example.moham.soleeklab.Utils.Constants.SPLASH_SCREEN_MILLI_SEC;
import static com.example.moham.soleeklab.Utils.Constants.TAG_SPLASH_ACTIVITY;

public class SplashActivity extends AppCompatActivity {

//    @BindView(R.id.iv_splash_screen)u

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        ButterKnife.bind(this);
        Log.d(TAG_SPLASH_ACTIVITY, "onCreate() has been instantiated");

//        getSupportActionBar().hide();

        checkUserSession();
    }


    private void checkUserSession() {
        Log.d(TAG_SPLASH_ACTIVITY, "checkUserSession() has been instantiated");

        try {
            Thread.sleep(SPLASH_SCREEN_MILLI_SEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Employee employee = EmployeeSharedPreferences.readEmployeeFromPreferences(this);

        if (employee != null) {
            Log.d(TAG_SPLASH_ACTIVITY, " Employee" + employee.toString());
            String token = employee.getToken();
            Log.d(TAG_SPLASH_ACTIVITY, "checkUserSession: Employee Token = " + token);

            if (!TextUtils.isEmpty(token)) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                this.finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                EmployeeSharedPreferences.clearPreferences(this);
                startActivity(intent);
                this.finish();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}