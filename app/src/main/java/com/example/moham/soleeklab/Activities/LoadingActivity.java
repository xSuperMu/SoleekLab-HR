package com.example.moham.soleeklab.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.moham.soleeklab.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_ACTIVITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;

public class LoadingActivity extends AppCompatActivity {


    private LoadingReceiver loadingReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d(TAG_LOADING_ACTIVITY, "onCreate() has been instantiated");

        Log.d(TAG_LOADING_ACTIVITY, "Registering the receiver");
        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CLOSE);
        loadingReceiver = new LoadingReceiver();
//        registerReceiver(loadingReceiver, filter);
        LocalBroadcastManager.getInstance(this).registerReceiver(loadingReceiver, filter);

        try {
            Log.d(TAG_LOADING_ACTIVITY, "Trying to Load the GIF");

            GifImageView gifImageView = findViewById(R.id.gif_loading);
            GifDrawable gifFromAssets = new GifDrawable(getAssets(), "logoloading.gif");

            gifImageView.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG_LOADING_ACTIVITY, "Failed to Load the GIF");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LOADING_ACTIVITY, "onDestroy() has been instantiated");
//        unregisterReceiver(loadingReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadingReceiver);
    }

    class LoadingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_LOADING_RECEIVER, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CLOSE)) {
                LoadingActivity.this.finish();
            }
        }
    }
}
