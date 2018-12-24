package com.example.moham.soleeklab.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moham.soleeklab.Model.CheckInResponse;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;
import com.github.abdularis.civ.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.STR_EMP_STATUS_ABSENCE;
import static com.example.moham.soleeklab.Utils.Constants.STR_EMP_STATUS_ATTEND;
import static com.example.moham.soleeklab.Utils.Constants.STR_EMP_STATUS_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_CHECK_IN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_HOME;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;

public class HomeFragment extends Fragment implements HomeFragInterface {

    @BindView(R.id.civ_user_profile_pic)
    CircleImageView civUserProfilePic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_job_title)
    TextView tvUserJobTitle;
    @BindView(R.id.iv_user_status_image)
    ImageView ivUserStatusImage;
    @BindView(R.id.tv_user_status_text)
    TextView tvUserStatusText;
    @BindView(R.id.ll_user_status)
    LinearLayout llUserStatus;
    @BindView(R.id.view_checkout_circle)
    View viewLogoutCircle;
    @BindView(R.id.cl_user_status_login)
    ConstraintLayout clUserStatusLogin;
    @BindView(R.id.tv_check_out_text)
    TextView tvCheckOutText;
    @BindView(R.id.tv_check_out_message)
    TextView tvCheckOutMessage;
    @BindView(R.id.ll_check_out)
    LinearLayout llCheckOut;
    @BindView(R.id.tv_task_this_week)
    TextView tvTaskThisWeek;
    @BindView(R.id.tv_task_progress_this_week)
    TextView tvTaskProgressThisWeek;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.fl_holder)
    FrameLayout flHolder;
    @BindView(R.id.tv_view_all_tasks)
    TextView tvViewAllTasks;
    Unbinder unbinder;
    @BindView(R.id.cl_task_progress)
    ConstraintLayout clTaskProgress;
    private CheckInResponse checkInResponse;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_HOME, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_HOME, "onDestroyView() has been instantiated");
        unbinder.unbind();

    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_HOME, "Loading CheckInResponse from the preferences");
        checkInResponse = EmployeeSharedPreferences.readCheckInResponseFromPreferences(getActivity());

        Log.d(TAG_FRAG_HOME, "checkInResponse Object ---->" + checkInResponse.toString());
        Log.d(TAG_FRAG_HOME, "User status ---->" + checkInResponse.getState());

        String status = checkInResponse.getState();
        /*
         *
         *  check user status
         *
         *  if status == absence ------> handleAbsenceState()
         *
         *  if status == vacation -----> handleVacationState()
         *
         *  if status == attend
         *  |
         *  |
         *  checkIn == null ---> normal state (show checkout)
         *  |
         *  |
         *  checkIn == data ---> handleZombieState()
         *
         * */

        if (status.equals(STR_EMP_STATUS_ABSENCE)) {
            handleAbsenceState();
        } else if (status.equals(STR_EMP_STATUS_ATTEND)) {
            handleAttendState();
        } else if (status.equals(STR_EMP_STATUS_VACATION)) {
            handleVacationState();
        }
    }

    @OnClick(R.id.view_checkout_circle)
    @Override
    public void handleCheckoutClick() {
        Log.d(TAG_FRAG_HOME, "handleCheckoutClick() has been instantiated");
    }

    @Override
    public void handleAbsenceState() {
        Log.d(TAG_FRAG_HOME, "handleAbsenceState() has been instantiated");
        llCheckOut.setVisibility(View.GONE);
        clTaskProgress.setVisibility(View.GONE);
        ivUserStatusImage.setImageDrawable(getResources().getDrawable(R.mipmap.research_1));
        llUserStatus.setVisibility(View.VISIBLE);

        Log.d(TAG_FRAG_CHECK_IN, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
    }

    @Override
    public void handleVacationState() {
        Log.d(TAG_FRAG_HOME, "handleVacationState() has been instantiated");
        llCheckOut.setVisibility(View.GONE);
        clTaskProgress.setVisibility(View.GONE);
        ivUserStatusImage.setImageDrawable(getResources().getDrawable(R.mipmap.ufo));
        llUserStatus.setVisibility(View.VISIBLE);

        Log.d(TAG_FRAG_CHECK_IN, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
    }

    @Override
    public void handleZombieState() {
        Log.d(TAG_FRAG_HOME, "handleZombieState() has been instantiated");
        llUserStatus.setVisibility(View.GONE);
        viewLogoutCircle.setVisibility(View.GONE);
        tvCheckOutText.setVisibility(View.GONE);
        llCheckOut.setVisibility(View.VISIBLE);
        clTaskProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleAttendState() {
        Log.d(TAG_FRAG_HOME, "handleAttendState() has been instantiated");
        llUserStatus.setVisibility(View.GONE);

        String checkout = null;
        try {
            checkout = checkInResponse.getCheckOut();
            Log.d(TAG_FRAG_HOME, "checkOut ----> " + checkout);
        } catch (NullPointerException e) {
            checkout = null;
        }

        if (checkout == null) {
            Log.d(TAG_FRAG_HOME, "checkOut ----> Null");
            Log.d(TAG_FRAG_HOME, "Show normal state");
            llCheckOut.setVisibility(View.VISIBLE);
            clTaskProgress.setVisibility(View.GONE);
        } else {
            Log.d(TAG_FRAG_HOME, "checkOut ----> " + checkout);
            Log.d(TAG_FRAG_HOME, "Show ZOMBIE state");
            handleZombieState();
        }

        Log.d(TAG_FRAG_CHECK_IN, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
    }
}
