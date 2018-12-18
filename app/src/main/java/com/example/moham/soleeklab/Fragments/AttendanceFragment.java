package com.example.moham.soleeklab.Fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moham.soleeklab.Adapter.AttendanceAdapter;
import com.example.moham.soleeklab.Interfaces.AttendanceFregInterface;
import com.example.moham.soleeklab.Model.Attendance;
import com.example.moham.soleeklab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_ATTENDANCE;

public class AttendanceFragment extends Fragment implements AttendanceFregInterface, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.tv_action_bar_attendance)
    TextView tvActionBarAttendance;
    @BindView(R.id.iv_no_attendance_icon)
    ImageView ivNoAttendanceIcon;
    @BindView(R.id.tv_no_attendance_text)
    TextView tvNoAttendanceText;
    @BindView(R.id.cl_no_attendance)
    ConstraintLayout clNoAttendance;
    @BindView(R.id.rv_attendance)
    RecyclerView rvAttendance;
    Unbinder unbinder;
    @BindView(R.id.srlAttendanceSwipe)
    SwipeRefreshLayout srlAttendanceSwipe;
    AttendanceAdapter mAttendanceAdapter;

    private List<Attendance> mUserAttendances;

    public AttendanceFragment() {
    }

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_ATTENDANCE, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_ATTENDANCE, "instantiateViews() has been instantiated");

        // RecyclerView object dependencies
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAttendance.setLayoutManager(mLinearLayoutManager);
        rvAttendance.setHasFixedSize(true);

        mAttendanceAdapter = new AttendanceAdapter(getActivity());
        rvAttendance.setAdapter(mAttendanceAdapter);

        srlAttendanceSwipe.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        srlAttendanceSwipe.post(new Runnable() {

            @Override
            public void run() {
                srlAttendanceSwipe.setRefreshing(true);
                // Fetching data from server
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_ATTENDANCE, "onDestroyView() has been instantiated");

        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG_FRAG_ATTENDANCE, "onRefresh() has been instantiated");
        // refresh the data
    }

    @Override
    public void loadAttendanceData() {
        Log.d(TAG_FRAG_ATTENDANCE, "loadAttendanceData() has been instantiated");
    }
}
