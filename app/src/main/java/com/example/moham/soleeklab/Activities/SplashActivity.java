package com.example.moham.soleeklab.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.moham.soleeklab.R;
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


        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("EMPLOYEE_TOKEN", "");
        Log.d(TAG_SPLASH_ACTIVITY, "checkUserSession: Employee Token = " + token);

        if (!TextUtils.isEmpty(token)) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            EmployeeSharedPreferences.clearPreferences(this);
            startActivity(intent);
            this.finish();
        }
    }
}