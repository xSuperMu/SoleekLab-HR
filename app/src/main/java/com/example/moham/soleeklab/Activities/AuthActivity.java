package com.example.moham.soleeklab.Activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.moham.soleeklab.Fragments.LoginFragment;
import com.example.moham.soleeklab.Interfaces.AuthActivitiyInterface;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_AUTH_ACTIVITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;

public class AuthActivity extends AppCompatActivity implements AuthActivitiyInterface {

    @BindView(R.id.iv_auth_background)
    ImageView ivAuthBackground;
    @BindView(R.id.sc_auth_activity)
    ScrollView scAuthActivity;

    private boolean doubleClickToExitPressedOnce = false;

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
        fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, LoginFragment.newInstance()).addToBackStack(TAG_FRAG_LOGIN).commit();
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

    @Override
    public void onBackPressed() {
        Log.d(TAG_AUTH_ACTIVITY, "onBackPressed() has been instantiated");

        if (doubleClickToExitPressedOnce) finish();

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.i(TAG_AUTH_ACTIVITY, "BackStackEntryCount() == " + count);

        if (count == 1) {
            Toast.makeText(this, "Click once more to close the app", Toast.LENGTH_SHORT).show();
            doubleClickToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClickToExitPressedOnce = false;
                }
            }, 2750);
        } else {
            clearBackStack();
            replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
        }

    }

    @Override
    public void clearBackStack() {
        Log.d(TAG_AUTH_ACTIVITY, "clearBackStack() has been instantiated");
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            Constants.sDisableFragmentAnimations = true;
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Constants.sDisableFragmentAnimations = false;
        } else
            Log.w(TAG_AUTH_ACTIVITY, "FragmentManager -> null");
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_AUTH_ACTIVITY, "replaceFragmentWithAnimation() has been instantiated");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


}