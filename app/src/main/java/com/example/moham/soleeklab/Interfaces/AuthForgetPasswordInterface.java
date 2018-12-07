package com.example.moham.soleeklab.Interfaces;

import android.support.v4.app.Fragment;

public interface AuthForgetPasswordInterface {

    boolean checkEmailValidation(String email);

    void instantiateViews();

    void showNoNetworkDialog();

    boolean isNetworkAvailable();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);
}
