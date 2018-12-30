package com.example.moham.soleeklab.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moham.soleeklab.R;

import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_ATTENDANCE;
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.STR_EXTRA_CODE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_ACTIVITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_REC;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_ATTENDANCE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

public class LoadingActivity extends AppCompatActivity {


    private int extraInt;
    private LoadingReceiver loadingReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d(TAG_LOADING_ACTIVITY, "onCreate() has been instantiated");

        Log.d(TAG_LOADING_ACTIVITY, "Registering the receiver");
        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN);
        loadingReceiver = new LoadingReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(loadingReceiver, filter);

        Intent intent = getIntent();
        if (getIntent() != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                extraInt = extras.getInt(STR_EXTRA_CODE);
            }
        }


        try {
            Log.d(TAG_LOADING_ACTIVITY, "Trying to Load the GIF");

            //  GifImageView gifImageView = findViewById(R.id.gif_loading);
            //  GifDrawable gifFromAssets = new GifDrawable(getAssets(), "logoloading.gif");

            ImageView imageView = findViewById(R.id.gif_loading);
            Glide.with(this).asGif().load(R.raw.logoloading).into(imageView).clearOnDetach();

            //  gifImageView.setImageDrawable(gifFromAssets);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG_LOADING_ACTIVITY, "Failed to Load the GIF");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LOADING_ACTIVITY, "onDestroy() has been instantiated");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadingReceiver);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG_LOADING_REC, "onBackPressed() has been instantiated");

        Log.d(TAG_LOADING_REC, "extraInt: " + extraInt);
        switch (extraInt) {
            case INT_CANCEL_LOGIN:
                Log.d(TAG_LOADING_REC, "Sending cancel login request broadcast");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CANCEL_LOGIN));
                break;
            case INT_CANCEL_FORGET_PASS:
                Log.d(TAG_LOADING_REC, "Sending cancel forget pass request broadcast");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CANCEL_FORGET_PASS));
                break;
            case INT_CANCEL_RESET_PASS:
                Log.d(TAG_LOADING_REC, "Sending cancel reset pass request broadcast");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CANCEL_RESET_PASS));
                break;
            case INT_CANCEL_VERIFY_IDENTITY:
                Log.d(TAG_LOADING_REC, "Sending cancel verify identity request broadcast");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CANCEL_VERIFY_IDENTITY));
                break;
            case INT_CANCEL_ATTENDANCE:
                Log.d(TAG_LOADING_REC, "Sending cancel getting attendance request broadcast");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CANCEL_ATTENDANCE));
                break;
            default:
                this.finish();
        }
    }

    class LoadingReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_LOADING_REC, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN)) {
                LoadingActivity.this.finish();
            }
        }
    }
}
