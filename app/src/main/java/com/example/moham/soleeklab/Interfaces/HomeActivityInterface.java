package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import retrofit2.Response;

public interface HomeActivityInterface {
    void buildFragmentsList();

    void switchFragment(int pos, String tag);

    void instantiateViews();

    void disableShiftMode(BottomNavigationView view);

    Fragment getVisibleFragment();

    void navigateToHomeOrCheckIn(int state);

    void handleCheckInResponseError(Context context, Response response);
}
