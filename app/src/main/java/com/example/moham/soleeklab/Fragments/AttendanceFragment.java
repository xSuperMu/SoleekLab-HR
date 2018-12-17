package com.example.moham.soleeklab.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moham.soleeklab.R;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_ATTENDANCE;

public class AttendanceFragment extends Fragment {


    public AttendanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_ATTENDANCE, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        return view;
    }

}
