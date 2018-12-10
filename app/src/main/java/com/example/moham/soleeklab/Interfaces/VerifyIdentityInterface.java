package com.example.moham.soleeklab.Interfaces;

import android.support.v4.app.Fragment;

public interface VerifyIdentityInterface {

    void instantiateViews();

    void verifyUser();

    void showNoNetworkDialog();

    boolean isNetworkAvailable();

    void replaceFragmentWithAnimation(Fragment fragment, String tag);

    void clearBackStack();
}
