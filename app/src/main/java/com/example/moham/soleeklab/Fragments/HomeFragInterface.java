package com.example.moham.soleeklab.Fragments;

import android.content.Context;
import android.graphics.Typeface;

public interface HomeFragInterface {
    void instantiateViews();

    void handleCheckoutClick();

    void handleAbsenceState();

    void handleVacationState();

    void handleAttendState();

    void handleZombieState();

    void setFontsToViews();

    Typeface loadFont(Context context, String fontPath);
}
