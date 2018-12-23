package com.example.moham.soleeklab.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moham.soleeklab.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_DONE;

public class TaskDoneFragment extends Fragment {

    //    @BindView(R.id.rv_tasks_done)
//    RecyclerView rvTasksDone;
//    @BindView(R.id.srlTaskDoneSwipe)
//    SwipeRefreshLayout srlTaskDoneSwipe;
    Unbinder unbinder;

    public TaskDoneFragment() {
    }

    public static TaskDoneFragment newInstance() {
        return new TaskDoneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_DONE, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_task_done, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_DONE, "onDestroyView() has been instantiated");

        unbinder.unbind();
    }
}
