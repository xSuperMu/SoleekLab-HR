package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;

public interface AttendanceFregInterface {

    void instantiateViews();

    void loadAttendanceData();

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();
}
