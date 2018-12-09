package com.example.moham.soleeklab.Activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.moham.soleeklab.Fragments.LoginFragment;
import com.example.moham.soleeklab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_AUTH_ACTIVITY;

public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.iv_auth_background)
    ImageView ivAuthBackground;
    @BindView(R.id.sc_auth_activity)
    ScrollView scAuthActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        Log.d(TAG_AUTH_ACTIVITY, "onCreate(): has been instantiated");

        getSupportActionBar().hide();

        scAuthActivity.post(new Runnable() {
            @Override
            public void run() {
                scAuthActivity.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_holder, LoginFragment.newInstance())
                .commit();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d(TAG_AUTH_ACTIVITY, "onWindowFocusChanged() has been instantiated");

        Bitmap loadedImage = ((BitmapDrawable) getResources().getDrawable(R.mipmap.bg)).getBitmap();
        int intendedWidth = ivAuthBackground.getWidth();

        // Gets the dimns of the image
        int originalWidth = loadedImage.getWidth();
        int originalHeight = loadedImage.getHeight();

        // Calculates the new dimensions
        float scale = (float) intendedWidth / originalWidth;
        int newHeight = (int) Math.round(originalHeight * scale);

        // Resizes mImageView. Change "FrameLayout" to whatever layout mImageView is located in.
        ivAuthBackground.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        ivAuthBackground.getLayoutParams().width = intendedWidth;
        ivAuthBackground.getLayoutParams().height = newHeight;

        Log.d(TAG_AUTH_ACTIVITY, "onWindowFocusChanged() has been returned");

    }
}