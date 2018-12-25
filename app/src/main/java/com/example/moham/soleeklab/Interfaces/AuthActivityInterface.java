package com.example.moham.soleeklab.Interfaces;

import android.support.v4.app.Fragment;

public interface AuthActivityInterface {

    void replaceFragmentWithAnimation(Fragment fragment, String tag);

    Fragment getVisibleFragment();
}
