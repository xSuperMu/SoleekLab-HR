package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.R;

import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_TASKS_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_TASKS;

public class TasksFragment extends Fragment {

    HomeActivity mHomeActivity;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_TASKS, "onCreateView() has been instantiated");
//        Log.d(TAG_FRAG_TASKS, "Backstack count ----> " + getActivity().getSupportFragmentManager().getBackStackEntryCount());

        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_TASKS_POS - 1).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_TASKS, "onAttach() has been instantiated");

        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }
}
