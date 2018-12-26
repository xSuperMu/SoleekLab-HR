package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
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
import com.example.moham.soleeklab.Interfaces.HomeFragInterface;
import com.example.moham.soleeklab.Model.CheckInResponse;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;
import com.github.abdularis.civ.CircleImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

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
    HomeActivity mHomeActivity;
    private CheckInResponse checkInResponse;
    private HeaderInjector headerInjector;

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

        headerInjector = new HeaderInjectorImplementation(getActivity());
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

        int status = checkInResponse.getState();
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
         *  checkOut == null ---> normal state (show checkout)
         *  |
         *  |
         *  checkOut == data ---> handleZombieState()
         *
         * */

        if (status == STR_EMP_STATUS_ABSENCE) {
            handleAbsenceState();
        } else if (status == STR_EMP_STATUS_ATTEND) {
            handleAttendState();
        } else if (status == STR_EMP_STATUS_VACATION) {
            handleVacationState();
        }
    }

    @OnClick(R.id.view_checkout_circle)
    @Override
    public void handleCheckoutClick() {
        Log.d(TAG_FRAG_HOME, "handleCheckoutClick() has been instantiated");

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_HOME, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        Log.d(TAG_FRAG_HOME, "Showing loading activity");
        startActivity(new Intent(getContext(), LoadingActivity.class));


        Log.e(TAG_FRAG_HOME, "Checking employee out");
        ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
        Call<CheckInResponse> call = service.checkOutUser(headerInjector.getHeaders());
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
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
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
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
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
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
    }

    @Override
    public void handleZombieState() {
        Log.d(TAG_FRAG_HOME, "handleZombieState() has been instantiated");
        llUserStatus.setVisibility(View.GONE);
        viewCheckoutCircle.setVisibility(View.GONE);
        tvCheckOutText.setVisibility(View.GONE);
        tvCheckOutMessage.setVisibility(View.GONE);
        clTaskProgress.setVisibility(View.GONE);

        checkInResponse = EmployeeSharedPreferences.readCheckInResponseFromPreferences(getActivity());

        String checkInTime = null;
        String checkOutTime = null;
        String totalWorkTime = null;

        try {
            checkInTime = formatTime(getActivity(), checkInResponse.getCheckIn());
            checkOutTime = formatTime(getActivity(), checkInResponse.getCheckOut());
            totalWorkTime = subtractDates(checkInResponse.getCheckOut(), checkInResponse.getCheckIn());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Update Views
        if (!TextUtils.isEmpty(checkInTime) && !TextUtils.isEmpty(totalWorkTime) && !TextUtils.isEmpty(checkOutTime)) {
            String chkInTimeStr = getString(R.string.checkin_msg, checkInTime);
            String chkOutTimeStr = getString(R.string.checkout_msg, checkOutTime);
            String totTimeStr = getString(R.string.total_time_msg, totalWorkTime);
            tvCheckInTime.setText(chkInTimeStr);
            tvCheckOutTime.setText(chkOutTimeStr);
            tvTotalWorkTime.setText(totTimeStr);
        }

        tvCheckInTime.setVisibility(View.VISIBLE);
        tvCheckOutTime.setVisibility(View.VISIBLE);
        tvTotalWorkTime.setVisibility(View.VISIBLE);
        llCheckOut.setVisibility(View.VISIBLE);

        Log.d(TAG_FRAG_HOME, "Hiding loading activity");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
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


            String checkInTime = null;
            String totalWorkTime = null;
            try {
                checkInTime = formatTime(getActivity(), checkInResponse.getCheckIn());

                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.original_date_format));
                String currentTime = sdf.format(new Date());

                totalWorkTime = subtractDates(currentTime, checkInResponse.getCheckIn());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Update Views
            if (!TextUtils.isEmpty(checkInTime) && !TextUtils.isEmpty(totalWorkTime)) {
                String checkInTimeStr = getString(R.string.check_out_messages, checkInTime, totalWorkTime);
                tvCheckOutMessage.setText(checkInTimeStr);
            }
            llCheckOut.setVisibility(View.VISIBLE);
            clTaskProgress.setVisibility(View.GONE);

        } else {
            Log.d(TAG_FRAG_HOME, "checkOut ----> " + checkout);
            Log.d(TAG_FRAG_HOME, "Show ZOMBIE state");
            handleZombieState();
        }

        Log.d(TAG_FRAG_HOME, "Hiding loading activity");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
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

    @Override
    public String formatTime(Context context, String timeToFormat) throws ParseException {
        Log.d(TAG_FRAG_HOME, "formatTime() has been instantiated");

        String pattern = context.getString(R.string.date_format);
        DateFormat formatter = new SimpleDateFormat(pattern);
        DateFormat originalFormat = new SimpleDateFormat(context.getString(R.string.original_date_format), Locale.ENGLISH);
        Date date = originalFormat.parse(timeToFormat);
        return formatter.format(date);
    }

    @Override
    public String subtractDates(String checkOut, String checkIn) throws ParseException {
        Log.d(TAG_FRAG_HOME, "subtractDates() has been instantiated");

        String totalTime = null;
        DateFormat originalFormat = new SimpleDateFormat(getString(R.string.original_date_format), Locale.ENGLISH);

        Date checkOutDate = originalFormat.parse(checkOut);
        Date checkInDate = originalFormat.parse(checkIn);

        long dateDifference = checkOutDate.getTime() - checkInDate.getTime();

        String diffHours = String.valueOf((int) (dateDifference / (60 * 60 * 1000)));
        String diffMins = String.valueOf((int) (dateDifference / (60 * 1000)));

        totalTime = diffHours + "h " + diffMins + "m";

        return totalTime;
    }

}
