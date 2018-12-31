package com.example.moham.soleeklab.Interfaces;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

public interface TasksFragInterface {

    void switchFragment(Fragment fragment, String tag);

    void handleDoneClicked();

    void handleToDoClicked();

    void instantiateViews();

    Typeface loadFont(Context context, String fontPath);

    void setFontsToViews();

    void handleFilterTasks();
}
