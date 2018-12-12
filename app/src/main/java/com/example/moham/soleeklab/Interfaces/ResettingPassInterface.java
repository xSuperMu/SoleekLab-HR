package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

import retrofit2.Response;

public interface ResettingPassInterface {

    boolean checkPasswordValidation(String password, int whichPassword);

    boolean checkPasswordMatch(String password, String reTypedPassword);

    void resetPassword();

    void instantiateViews();

    void showNoNetworkDialog();

    boolean isNetworkAvailable();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);

    void clearBackStack();

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();

    void getResponseErrorMessage(Context context, Response response);
}
