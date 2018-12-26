package com.example.moham.soleeklab.Fragments;

import android.content.Context;
import android.graphics.Typeface;

import java.text.ParseException;

public interface HomeFragInterface {
    void instantiateViews();

    void handleCheckoutClick();

    void handleAbsenceState();

    void handleVacationState();

    void handleAttendState();

    void handleZombieState();

    void setFontsToViews();

    Typeface loadFont(Context context, String fontPath);

    String formatTime(Context context, String timeToFormat) throws ParseException;

    String subtractDates(String checkIn, String checkOut) throws ParseException;
}
