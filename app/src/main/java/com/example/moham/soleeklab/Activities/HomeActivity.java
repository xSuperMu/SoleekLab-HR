package com.example.moham.soleeklab.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.moham.soleeklab.Fragments.AttendanceFragment;
import com.example.moham.soleeklab.Fragments.CheckInFragment;
import com.example.moham.soleeklab.Fragments.FeedbackFragment;
import com.example.moham.soleeklab.Fragments.HomeFragment;
import com.example.moham.soleeklab.Fragments.MoreFragment;
import com.example.moham.soleeklab.Fragments.NewVacationFragment;
import com.example.moham.soleeklab.Fragments.NotificationFragment;
import com.example.moham.soleeklab.Fragments.TaskDoneFragment;
import com.example.moham.soleeklab.Fragments.TaskToDoFragment;
import com.example.moham.soleeklab.Fragments.TasksFragment;
import com.example.moham.soleeklab.Fragments.VacationFragment;
import com.example.moham.soleeklab.Interfaces.HomeActivityInterface;
import com.example.moham.soleeklab.Model.Responses.CheckInResponse;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.INT_BTN_NAV_FRAGMENTS_COUNT;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_CHECK_IN_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_HOME_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_MORE_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_NOTIFICATIONS_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_TASKS_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_VACATION_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_CHECK_IN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_HOME;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_MORE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NOTIFICATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_TASKS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_HOME_ACTIVITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

public class HomeActivity extends AppCompatActivity implements HomeActivityInterface {

    @BindView(R.id.bottom_navigation_view)
    public BottomNavigationView bnvNavigation;
    private List<Fragment> mFragmentsList = new ArrayList<>(INT_BTN_NAV_FRAGMENTS_COUNT);
    private boolean doubleClickToExitPressedOnce = false;
    private int responseCode;
    private HeaderInjector headerInjector;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            CheckInResponse response = null;
                            int state;
                            try {
                                Log.d(TAG_HOME_ACTIVITY, "trying to load CheckInResponse from preferences----> ");
                                response = EmployeeSharedPreferences.readCheckInResponseFromPreferences(HomeActivity.this);
                                Log.d(TAG_HOME_ACTIVITY, "CheckInResponse toString() ----> " + response.toString());
                                state = response.getState();
                                Log.d(TAG_HOME_ACTIVITY, "CheckInResponse state toString() ----> " + response.getState());
                            } catch (NullPointerException e) {
                                Log.d(TAG_HOME_ACTIVITY, "CheckInResponse state ----> NULL");
                                state = 0;
                            }
                            if (state == 0)
                                switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
                            else
                                switchFragment(INT_FRAGMENT_HOME_POS, TAG_FRAG_HOME);
                            return true;
                        case R.id.navigation_vacation:
                            switchFragment(INT_FRAGMENT_VACATION_POS, TAG_FRAG_VACATION);
                            return true;
                        case R.id.navigation_task:
                            switchFragment(INT_FRAGMENT_TASKS_POS, TAG_FRAG_TASKS);
                            return true;
                        case R.id.navigation_notification:
                            switchFragment(INT_FRAGMENT_NOTIFICATIONS_POS, TAG_FRAG_NOTIFICATION);
                            return true;
                        case R.id.navigation_more:
                            switchFragment(INT_FRAGMENT_MORE_POS, TAG_FRAG_MORE);
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Log.d(TAG_HOME_ACTIVITY, "onCreate() has been instantiated");

        getSupportActionBar().hide();

        Intent loginExtra = getIntent();
        responseCode = loginExtra.getIntExtra("response code", 0);
        Log.d(TAG_HOME_ACTIVITY, "LoginExtra: response code --> " + responseCode);

        instantiateViews();
    }

    @Override
    public void buildFragmentsList() {
        Log.d(TAG_HOME_ACTIVITY, "buildFragmentsList() has been instantiated");
        mFragmentsList.add(CheckInFragment.newInstance());
        mFragmentsList.add(HomeFragment.newInstance());
        mFragmentsList.add(VacationFragment.newInstance());
        mFragmentsList.add(TasksFragment.newInstance());
        mFragmentsList.add(NotificationFragment.newInstance());
        mFragmentsList.add(MoreFragment.newInstance());
    }

    @Override
    public void switchFragment(int pos, final String tag) {
        Log.d(TAG_HOME_ACTIVITY, "switchFragment() has been instantiated");
        doubleClickToExitPressedOnce = false;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, mFragmentsList.get(pos));
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG_HOME_ACTIVITY, "onBackPressed() has been instantiated");
        if (doubleClickToExitPressedOnce) finish();

        CheckInResponse response = null;
        int state;
        try {
            Log.d(TAG_HOME_ACTIVITY, "onBackPressed: trying to load CheckInResponse from preferences----> ");
            response = EmployeeSharedPreferences.readCheckInResponseFromPreferences(this);
            Log.d(TAG_HOME_ACTIVITY, "onBackPressed: CheckInResponse toString() ----> " + response.toString());
            state = response.getState();
            Log.d(TAG_HOME_ACTIVITY, "onBackPressed: CheckInResponse state toString() ----> " + response.getState());
        } catch (NullPointerException e) {
            Log.d(TAG_HOME_ACTIVITY, "onBackPressed: CheckInResponse state ----> NULL");
            state = 0;
        }

        Fragment fragment = getVisibleFragment();
        if (fragment instanceof HomeFragment || fragment instanceof CheckInFragment) {
            Toast.makeText(this, "Click once more to close the app", Toast.LENGTH_SHORT).show();
            doubleClickToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleClickToExitPressedOnce = false;
                }
            }, 2750);
        } else if (fragment instanceof VacationFragment || fragment instanceof TasksFragment || fragment instanceof NotificationFragment || fragment instanceof MoreFragment || fragment instanceof TaskToDoFragment || fragment instanceof TaskDoneFragment) {
            navigateToHomeOrCheckIn(state);
        } else if (fragment instanceof NewVacationFragment) {
            switchFragment(INT_FRAGMENT_VACATION_POS, TAG_FRAG_VACATION);
        } else if (fragment instanceof AttendanceFragment || fragment instanceof FeedbackFragment) {
            switchFragment(INT_FRAGMENT_MORE_POS, TAG_FRAG_MORE);
        } else {
            navigateToHomeOrCheckIn(state);
        }
    }

    @Override
    public void navigateToHomeOrCheckIn(int state) {
        Log.d(TAG_HOME_ACTIVITY, "navigateToHomeOrCheckIn() has been instantiated");

        if (state == 0)
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        else
            switchFragment(INT_FRAGMENT_HOME_POS, TAG_HOME_ACTIVITY);
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_HOME_ACTIVITY, "instantiateViews() has been instantiated");

        headerInjector = new HeaderInjectorImplementation(this);

        bnvNavigation.getMenu().getItem(INT_FRAGMENT_CHECK_IN_POS).setChecked(true);
        bnvNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        disableShiftMode(bnvNavigation);

        buildFragmentsList();

        // Navigate to the appropriate Fragment
        if (responseCode == 200) {
            switchFragment(INT_FRAGMENT_HOME_POS, TAG_FRAG_HOME);
        } else if (responseCode == 404) {
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        } else {
            Log.d(TAG_HOME_ACTIVITY, "ERROR! Response code = " + responseCode);

            Log.d(TAG_HOME_ACTIVITY, "Showing loading screen");
            startActivity(new Intent(HomeActivity.this, LoadingActivity.class));

            Log.d(TAG_HOME_ACTIVITY, "SERVICE: Getting today attendance");
            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            Call<CheckInResponse> callGetAttendance = service.getTodayAttendance(headerInjector.getHeaders());
            callGetAttendance.enqueue(new Callback<CheckInResponse>() {
                @Override
                public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_LOGIN, "Response code -> " + response.code() + " " + response.message() + " ");
                        // Save object to preferences
                        EmployeeSharedPreferences.SaveCheckInResponseToPreferences(HomeActivity.this, response.body());

                        Log.d(TAG_HOME_ACTIVITY, "Loading CheckInResponse from preferences");
                        CheckInResponse checkInResponse = EmployeeSharedPreferences.readCheckInResponseFromPreferences(HomeActivity.this);
                        int data;
                        try {
                            data = checkInResponse.getState();
                        } catch (NullPointerException e) {
                            data = 0;
                        }

                        if (data == 0) {
                            Log.d(TAG_HOME_ACTIVITY, "CheckInResponse Data ---> Null");
                            Log.d(TAG_HOME_ACTIVITY, "Navigating to CheckInFragment");
                            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
                        } else {
                            Log.d(TAG_HOME_ACTIVITY, "CheckInResponse Data ---> NOT Null");
                            Log.d(TAG_HOME_ACTIVITY, "Navigating to HomeFragment");

                            switchFragment(INT_FRAGMENT_HOME_POS, TAG_FRAG_HOME);
                        }
                        Log.d(TAG_HOME_ACTIVITY, "Hiding loading activity");
//                        sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                        LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    } else {
                        handleCheckInResponseError(HomeActivity.this, response);
                    }
                }

                @Override
                public void onFailure(Call<CheckInResponse> call, Throwable t) {
                    Log.e(TAG_HOME_ACTIVITY, "onFailure(): " + t.toString());
//                    sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    Toast.makeText(HomeActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Log.d(TAG_HOME_ACTIVITY, "instantiateViews() has been returned");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void disableShiftMode(BottomNavigationView view) {
        Log.wtf(TAG_HOME_ACTIVITY, "disableShiftMode() has been instantiated");

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG_HOME_ACTIVITY, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e(TAG_HOME_ACTIVITY, "Unable to change value of shift mode");
        }
    }

    @Override
    public Fragment getVisibleFragment() {
        Log.d(TAG_HOME_ACTIVITY, "getVisibleFragment() has been instantiated");
        FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments)
            if (fragment != null && fragment.isVisible())
                return fragment;
        return null;
    }

    @Override
    public void handleCheckInResponseError(Context context, Response response) {
        Log.d(TAG_HOME_ACTIVITY, "handleCheckInResponseError() has been instantiated");
        if (response.code() == 404) {
            Log.d(TAG_HOME_ACTIVITY, "Response code ------> " + response.code() + " " + response.message());
            Log.d(TAG_HOME_ACTIVITY, "User Doesn't Checked In Before, Moving to MainActivity --> CheckIn Fragment");
            try {
                Gson gson = new Gson();
                CheckInResponse checkInErrorModel = gson.fromJson(response.errorBody().string(), CheckInResponse.class);
                if (checkInErrorModel.getMessage() != null) {
                    Log.i(TAG_HOME_ACTIVITY, "handleCheckInResponseError() errorModel.Message = " + checkInErrorModel.getMessage());

                    // Save object to preferences
                    EmployeeSharedPreferences.SaveCheckInResponseToPreferences(HomeActivity.this, checkInErrorModel);


                    Log.d(TAG_HOME_ACTIVITY, "Loading CheckInResponse from preferences");
                    CheckInResponse checkInResponse = EmployeeSharedPreferences.readCheckInResponseFromPreferences(HomeActivity.this);
                    int data;
                    try {
                        data = checkInResponse.getState();
                    } catch (NullPointerException e) {
                        data = 0;
                    }

                    if (data == 0) {
                        Log.d(TAG_HOME_ACTIVITY, "CheckInResponse Data ---> Null");
                        Log.d(TAG_HOME_ACTIVITY, "Navigating to CheckInFragment");
                        switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
                    } else {
                        Log.d(TAG_HOME_ACTIVITY, "CheckInResponse Data ---> NOT Null");
                        Log.d(TAG_HOME_ACTIVITY, "Navigating to HomeFragment");
                        switchFragment(INT_FRAGMENT_HOME_POS, TAG_FRAG_HOME);
                    }
                    Log.d(TAG_HOME_ACTIVITY, "Hiding loading activity");
//                    sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
