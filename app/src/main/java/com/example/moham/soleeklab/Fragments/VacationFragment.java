package com.example.moham.soleeklab.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moham.soleeklab.R;

public class VacationFragment extends Fragment {


    public VacationFragment() {
    }


    public static VacationFragment newInstance() {
        return new VacationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vacation, container, false);
    }

}
