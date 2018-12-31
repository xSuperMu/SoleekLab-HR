package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;

import retrofit2.Response;

public interface AttendanceFregInterface {

    void instantiateViews();

    void loadAttendanceData();

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();

    void handleFilterAttendance();

    void handleAttendanceSheetResponseError(Context context, Response response);
}
