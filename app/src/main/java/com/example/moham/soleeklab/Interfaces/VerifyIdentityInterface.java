package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

import retrofit2.Response;

public interface VerifyIdentityInterface {

    void instantiateViews();

    void verifyUser();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();

    void getResponseErrorMessage(Context context, Response response);

    void enableViews();

    void disableViews();
}
