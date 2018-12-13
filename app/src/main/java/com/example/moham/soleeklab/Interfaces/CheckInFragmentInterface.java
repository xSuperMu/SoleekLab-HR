package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

public interface CheckInFragmentInterface {

    void replaceFragmentWithAnimation(Fragment fragment, String tag);

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();

    void handleCheckIn();
}
