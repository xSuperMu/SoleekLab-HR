package com.example.moham.soleeklab.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        try {
            Thread.sleep(SPLASH_SCREEN_MILLI_SEC);
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}