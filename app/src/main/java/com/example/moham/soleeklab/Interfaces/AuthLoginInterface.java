package com.example.moham.soleeklab.Interfaces;

import android.support.v4.app.Fragment;

public interface AuthLoginInterface {

    boolean checkEmailValidation(String email);

    boolean checkPasswordValidation(String password);

    void tryToLogin();

    void instantiateViews();

    boolean isNetworkAvailable();

    void showNoNetworkDialog();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);
}