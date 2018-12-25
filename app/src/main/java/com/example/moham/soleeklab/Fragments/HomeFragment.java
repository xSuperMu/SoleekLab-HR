package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Model.CheckInResponse;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;
import com.github.abdularis.civ.CircleImageView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_REGULAR;
import static com.example.moham.soleeklab.Utils.Constants.FONT_LIBREFRANKLIN_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_CHECK_IN_POS;
import static com.example.moham.soleeklab.Utils.Constants.STR_EMP_STATUS_ABSENCE;
import static com.example.moham.soleeklab.Utils.Constants.STR_EMP_STATUS_ATTEND;
import static com.example.moham.soleeklab.Utils.Constants.STR_EMP_STATUS_VACATION;
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
    View viewCheckoutCircle;
    @BindView(R.id.cl_user_status_checkout)
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
    @BindView(R.id.tv_check_in_time)
    TextView tvCheckInTime;
    @BindView(R.id.tv_check_out_time)
    TextView tvCheckOutTime;
    @BindView(R.id.tv_total_work_time)
    TextView tvTotalWorkTime;
    private CheckInResponse checkInResponse;
    HomeActivity mHomeActivity;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_HOME, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_CHECK_IN_POS).setChecked(true);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_HOME, "onAttach() has been instantiated");
        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_HOME, "onDestroyView() has been instantiated");
        unbinder.unbind();

    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_HOME, "instantiateViews() has been instantiated");

        Log.d(TAG_FRAG_HOME, "Loading CheckInResponse from the preferences");
        setFontsToViews();
        checkInResponse = EmployeeSharedPreferences.readCheckInResponseFromPreferences(getActivity());

        Log.d(TAG_FRAG_HOME, "Loading CheckInResponse from the preferences");
        Employee curEmp = EmployeeSharedPreferences.readEmployeeFromPreferences(getActivity());

        String empJobTitle = curEmp.getUser().getJobTitle();
        Log.d(TAG_FRAG_HOME, "empJobTitle ------>" + empJobTitle);
        tvUserJobTitle.setText(empJobTitle);

        String empName = curEmp.getUser().getName();
        Log.d(TAG_FRAG_HOME, "empName ------>" + empName);
        tvUserName.setText(empName);

        String empProfilePicStr = curEmp.getUser().getProfilePic();
        Log.d(TAG_FRAG_HOME, "empProfilePicStr ------>" + empProfilePicStr);
        Glide.with(this).load(empProfilePicStr).apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL)).into(civUserProfilePic);

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

        Log.d(TAG_FRAG_HOME, "Showing loading activity");
        startActivity(new Intent(getContext(), LoadingActivity.class));

        Log.e(TAG_FRAG_HOME, "Reading employee data from preferences");
        Employee curEmp = EmployeeSharedPreferences.readEmployeeFromPreferences(getActivity());

        String empToken = curEmp.getToken();
        Log.e(TAG_FRAG_HOME, "Employee token --> " + empToken);

        Log.e(TAG_FRAG_HOME, "Checking employee out");
        ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);

        String KEY_CONTENT_TYPE_HEADER = "Content-Type";
        String KEY_AUTH_HEADER = "Authorization";
        String KEY_Bearer = "Bearer ";
        String APPLICATION_JSON = "application/json";

        HashMap<String, String> headers = new HashMap<>();
        headers.put(KEY_CONTENT_TYPE_HEADER, APPLICATION_JSON);
        headers.put(KEY_AUTH_HEADER, KEY_Bearer + empToken);

        Log.d(TAG_FRAG_HOME, "SERVICE: checkOut");
        Call<CheckInResponse> call = service.checkOutUser(headers);
        call.enqueue(new Callback<CheckInResponse>() {
            @Override
            public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG_FRAG_HOME, "Response code -> " + response.code() + " " + response.message() + " ");
                    Log.d(TAG_FRAG_HOME, "Saving CheckInResponse object to preferences");
                    EmployeeSharedPreferences.SaveCheckInResponseToPreferences(getActivity(), response.body());

                    // Hide Checkout Button
                    handleZombieState();
                }
            }

            @Override
            public void onFailure(Call<CheckInResponse> call, Throwable t) {
                Log.e(TAG_FRAG_HOME, "onFailure(): " + t.toString());
                getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void handleAbsenceState() {
        Log.d(TAG_FRAG_HOME, "handleAbsenceState() has been instantiated");
        llCheckOut.setVisibility(View.GONE);
        clTaskProgress.setVisibility(View.GONE);
        ivUserStatusImage.setImageDrawable(getResources().getDrawable(R.mipmap.research_1));
        llUserStatus.setVisibility(View.VISIBLE);


        tvUserStatusText.setText(getString(R.string.absence_text));
        Log.d(TAG_FRAG_HOME, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
    }

    @Override
    public void handleVacationState() {
        Log.d(TAG_FRAG_HOME, "handleVacationState() has been instantiated");
        llCheckOut.setVisibility(View.GONE);
        clTaskProgress.setVisibility(View.GONE);
        ivUserStatusImage.setImageDrawable(getResources().getDrawable(R.mipmap.ufo));
        llUserStatus.setVisibility(View.VISIBLE);

        tvUserStatusText.setText(getString(R.string.vacation_text));
        Log.d(TAG_FRAG_HOME, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
    }

    @Override
    public void handleZombieState() {
        Log.d(TAG_FRAG_HOME, "handleZombieState() has been instantiated");
        llUserStatus.setVisibility(View.GONE);
        viewCheckoutCircle.setVisibility(View.GONE);
        tvCheckOutText.setVisibility(View.GONE);
        tvCheckOutMessage.setVisibility(View.GONE);
        tvCheckInTime.setVisibility(View.VISIBLE);
        tvCheckOutTime.setVisibility(View.VISIBLE);
        tvTotalWorkTime.setVisibility(View.VISIBLE);
        llCheckOut.setVisibility(View.VISIBLE);
        clTaskProgress.setVisibility(View.GONE);

        Log.d(TAG_FRAG_HOME, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
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

        Log.d(TAG_FRAG_HOME, "Hiding loading activity");
        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_HOME, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_HOME, "setFontsToViews() has been instantiated");
        tvUserName.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvUserJobTitle.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvUserStatusText.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvCheckOutText.setTypeface(loadFont(getActivity(), FONT_LIBREFRANKLIN_MEDIUM));
        tvCheckOutMessage.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvCheckInTime.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvCheckOutTime.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvTotalWorkTime.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvTaskThisWeek.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvTaskProgressThisWeek.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
        tvViewAllTasks.setTypeface(loadFont(getActivity(), FONT_DOSIS_REGULAR));

    }
}
