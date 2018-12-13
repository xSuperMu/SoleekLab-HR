package com.example.moham.soleeklab.Interfaces;

import android.support.design.widget.BottomNavigationView;

public interface HomeActivityInterface {
    void buildFragmentsList();

    void switchFragment(int pos, String tag);

    void instantiateViews();

    void disableShiftMode(BottomNavigationView view);
}
