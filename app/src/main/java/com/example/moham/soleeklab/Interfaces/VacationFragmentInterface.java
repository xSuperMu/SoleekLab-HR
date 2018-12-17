package com.example.moham.soleeklab.Interfaces;

import android.support.v4.app.Fragment;

public interface VacationFragmentInterface {
    void handleNewVacation();

    void switchFragment(Fragment fragment, final String tag);
}
