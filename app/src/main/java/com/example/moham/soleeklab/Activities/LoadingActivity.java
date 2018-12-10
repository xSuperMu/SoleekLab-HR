package com.example.moham.soleeklab.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.moham.soleeklab.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_ACTIVITY;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d(TAG_LOADING_ACTIVITY, "onCreate() has been instantiated");

        try {
            GifImageView gifImageView = findViewById(R.id.gif_loading);
            GifDrawable gifFromAssets = new GifDrawable(getAssets(), "logoloading.gif");

            gifImageView.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
