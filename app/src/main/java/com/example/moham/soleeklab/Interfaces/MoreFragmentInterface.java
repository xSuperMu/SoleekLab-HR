package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

public interface MoreFragmentInterface {

    void handleAttendaceClick();

    void handleFeedbackClick();

    void handleLogoutClick();

    void switchFragment(Fragment fragment, final String tag);

    void showConfirmLogoutDialog();

    Typeface loadFont(Context context, String fontPath);
}