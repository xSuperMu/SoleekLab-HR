package com.example.moham.soleeklab.Interfaces;

import android.support.v4.app.Fragment;

public interface ResettingPassInterface {

    boolean checkPasswordValidation(String password, int whichPassword);

    boolean checkPasswordMatch(String password, String reTypedPassword);

    void resetPassword();

    void instantiateViews();

    void showNoNetworkDialog();

    boolean isNetworkAvailable();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);
}
