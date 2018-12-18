package com.example.moham.soleeklab.Fragments;

import android.content.Context;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_BOLD;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_MEDIUM;
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

        setFontsToViews();

        // RecyclerView object dependencies
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAttendance.setLayoutManager(mLinearLayoutManager);
        rvAttendance.setHasFixedSize(true);
        srlAttendanceSwipe.setOnRefreshListener(this);
        mAttendanceAdapter = new AttendanceAdapter(getActivity());
        rvAttendance.setAdapter(mAttendanceAdapter);

        srlAttendanceSwipe.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        srlAttendanceSwipe.setRefreshing(true);
        loadAttendanceData();

        Log.d(TAG_FRAG_ATTENDANCE, "instantiateViews() return");
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
        srlAttendanceSwipe.setRefreshing(true);
        mAttendanceAdapter.swapAttendanceDataList(null);
        loadAttendanceData();
    }

    @Override
    public void loadAttendanceData() {
        Log.d(TAG_FRAG_ATTENDANCE, "loadAttendanceData() has been instantiated");

        List<Attendance> list = new ArrayList<>();
        list.add(new Attendance("Today, ", "31/08/2018", "09:38 AM", "06:59 PM", "8h 32m", null));
        list.add(new Attendance("Yesterday, ", "30/08/2018", "09:38 AM", "NA", "NA", null));
        list.add(new Attendance("Tuesday, ", "29/08/2018", null, null, null, "Absence"));
        list.add(new Attendance("Monday, ", "28/08/2018", null, null, null, "Vacation"));
        list.add(new Attendance("Sunday, ", "27/08/2018", "09:38 AM", "06:59 PM", "8h 32m", null));
        list.add(new Attendance("Saturday, ", "26/08/2018", "09:38 AM", "06:59 PM", "8h 32m", null));

        mAttendanceAdapter.swapAttendanceDataList(list);
        srlAttendanceSwipe.setRefreshing(false);

    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_ATTENDANCE, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_ATTENDANCE, "setFontsToViews() has been instantiated");
        tvActionBarAttendance.setTypeface(loadFont(getActivity(), FONT_DOSIS_BOLD));
        tvNoAttendanceText.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvNoAttendanceText.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
    }
}
