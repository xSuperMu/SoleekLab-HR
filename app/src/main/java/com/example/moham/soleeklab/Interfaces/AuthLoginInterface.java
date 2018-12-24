package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

import retrofit2.Response;

public interface AuthLoginInterface {

    boolean checkEmailValidation(String email);

    boolean checkPasswordValidation(String password);

    void tryToLogin();

    void instantiateViews();

    boolean isNetworkAvailable();

    void showNoNetworkDialog();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();

    void getLoginResponseErrorMessage(Context context, Response response);

    void handleCheckInResponseError(Context context, Response response);
}