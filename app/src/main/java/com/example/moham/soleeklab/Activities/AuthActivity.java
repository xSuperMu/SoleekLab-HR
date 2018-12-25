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

import com.example.moham.soleeklab.Fragments.ForgetPasswordFragment;
import com.example.moham.soleeklab.Fragments.LoginFragment;
import com.example.moham.soleeklab.Fragments.ResettingPasswordFragment;
import com.example.moham.soleeklab.Fragments.VerifyIdentityFragment;
import com.example.moham.soleeklab.Interfaces.AuthActivityInterface;
import com.example.moham.soleeklab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_AUTH_ACTIVITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;

public class AuthActivity extends AppCompatActivity implements AuthActivityInterface {

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
                .add(R.id.fragment_holder, LoginFragment.newInstance()).commit();
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

        Fragment fragment = getVisibleFragment();
        if (fragment instanceof LoginFragment) {
            Toast.makeText(this, "Click once more to close the app", Toast.LENGTH_SHORT).show();
            doubleClickToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClickToExitPressedOnce = false;
                }
            }, 2750);
        } else if (fragment instanceof ForgetPasswordFragment || fragment instanceof ResettingPasswordFragment || fragment instanceof VerifyIdentityFragment)
            replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
        else {
            replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
        }
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_AUTH_ACTIVITY, "replaceFragmentWithAnimation() has been instantiated");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.commit();
    }

    @Override
    public Fragment getVisibleFragment() {
        Log.d(TAG_AUTH_ACTIVITY, "getVisibleFragment() has been instantiated");
        FragmentManager fragmentManager = AuthActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments)
            if (fragment != null && fragment.isVisible())
                return fragment;
        return null;
    }
}