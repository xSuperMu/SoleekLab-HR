package com.example.moham.soleeklab.Interfaces;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

public interface HomeActivityInterface {
    void buildFragmentsList();

    void switchFragment(int pos, String tag);

    void instantiateViews();

    void disableShiftMode(BottomNavigationView view);

    Fragment getVisibleFragment();
}
