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

//        Log.d(TAG_SPLASH_ACTIVITY, "onWindowFocusChanged() has been instantiated");
//
//        Bitmap loadedImage = ((BitmapDrawable) getResources().getDrawable(R.mipmap.splash)).getBitmap();
//        int intendedWidth = ivSplashScreen.getWidth();
//
//        // Gets the dimns of the image
//        int originalWidth = loadedImage.getWidth();
//        int originalHeight = loadedImage.getHeight();
//
//        // Calculates the new dimensions
//        float scale = (float) intendedWidth / originalWidth;
//        int newHeight = (int) Math.round(originalHeight * scale);
//
//        // Resizes mImageView. Change "FrameLayout" to whatever layout mImageView is located in.
//        ivSplashScreen.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                ConstraintLayout.LayoutParams.WRAP_CONTENT));
//
//        ivSplashScreen.getLayoutParams().width = intendedWidth;
//        ivSplashScreen.getLayoutParams().height = newHeight;
//
//        Log.d(TAG_SPLASH_ACTIVITY, "onWindowFocusChanged() has been returned");

        try {
            Thread.sleep(SPLASH_SCREEN_MILLI_SEC);
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        Log.d(TAG_SPLASH_ACTIVITY, "onWindowFocusChanged() has been instantiated");
//
//        Bitmap loadedImage = ((BitmapDrawable) getResources().getDrawable(R.mipmap.splash)).getBitmap();
//        int intendedWidth = ivSplashScreen.getWidth();
//
//        // Gets the dimns of the image
//        int originalWidth = loadedImage.getWidth();
//        int originalHeight = loadedImage.getHeight();
//
//        // Calculates the new dimensions
//        float scale = (float) intendedWidth / originalWidth;
//        int newHeight = (int) Math.round(originalHeight * scale);
//
//        // Resizes mImageView. Change "FrameLayout" to whatever layout mImageView is located in.
//        ivSplashScreen.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                ConstraintLayout.LayoutParams.WRAP_CONTENT));
//
//        ivSplashScreen.getLayoutParams().width = intendedWidth;
//        ivSplashScreen.getLayoutParams().height = newHeight;
//
//        Log.d(TAG_SPLASH_ACTIVITY, "onWindowFocusChanged() has been returned");
//    }

}
