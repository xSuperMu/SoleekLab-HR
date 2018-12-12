package com.example.moham.soleeklab.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.moham.soleeklab.R;

import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));

    }
}
