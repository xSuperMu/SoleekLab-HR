package com.example.moham.soleeklab.Activities;

import android.annotation.SuppressLint;
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
import com.example.moham.soleeklab.Fragments.TasksFragment;
import com.example.moham.soleeklab.Fragments.VacationFragment;
import com.example.moham.soleeklab.Interfaces.HomeActivityInterface;
import com.example.moham.soleeklab.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import static com.example.moham.soleeklab.Utils.Constants.INT_BTN_NAV_FRAGMENTS_COUNT;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_CHECK_IN_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_MORE_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_NOTIFICATIONS_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_TASKS_POS;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_VACATION_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_CHECK_IN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_MORE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NOTIFICATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_TASKS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_HOME_ACTIVITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;

public class HomeActivity extends AppCompatActivity implements HomeActivityInterface {

    @BindView(R.id.bottom_navigation_view)
    public BottomNavigationView bnvNavigation;
    private List<Fragment> mFragmentsList = new ArrayList<>(INT_BTN_NAV_FRAGMENTS_COUNT);
    private boolean doubleClickToExitPressedOnce = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
                            Log.d(TAG_HOME_ACTIVITY, "Backstack count ----> " + getSupportFragmentManager().getBackStackEntryCount());
                            return true;
                        case R.id.navigation_vacation:
                            switchFragment(INT_FRAGMENT_VACATION_POS, TAG_FRAG_VACATION);
                            Log.d(TAG_HOME_ACTIVITY, "Backstack count ----> " + getSupportFragmentManager().getBackStackEntryCount());
                            return true;
                        case R.id.navigation_task:
                            switchFragment(INT_FRAGMENT_TASKS_POS, TAG_FRAG_TASKS);
                            Log.d(TAG_HOME_ACTIVITY, "Backstack count ----> " + getSupportFragmentManager().getBackStackEntryCount());
                            return true;
                        case R.id.navigation_notification:
                            switchFragment(INT_FRAGMENT_NOTIFICATIONS_POS, TAG_FRAG_NOTIFICATION);
                            Log.d(TAG_HOME_ACTIVITY, "Backstack count ----> " + getSupportFragmentManager().getBackStackEntryCount());
                            return true;
                        case R.id.navigation_more:
                            switchFragment(INT_FRAGMENT_MORE_POS, TAG_FRAG_MORE);
                            Log.d(TAG_HOME_ACTIVITY, "Backstack count ----> " + getSupportFragmentManager().getBackStackEntryCount());
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

        // Clear fragment manager backstack
        getSupportFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
        Log.d(TAG_HOME_ACTIVITY, "BackStack count -------> " + getSupportFragmentManager().getBackStackEntryCount());

        // To hide the loading screen
        sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));

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
        transaction.replace(R.id.frame_fragment_holder, mFragmentsList.get(pos), tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG_HOME_ACTIVITY, "onBackPressed() has been instantiated");
        if (doubleClickToExitPressedOnce) finish();


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
        } else if (fragment instanceof VacationFragment) {
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        } else if (fragment instanceof NewVacationFragment) {
            switchFragment(INT_FRAGMENT_VACATION_POS, TAG_FRAG_VACATION);
        } else if (fragment instanceof TasksFragment) {
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        } else if (fragment instanceof NotificationFragment) {
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        } else if (fragment instanceof MoreFragment) {
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        } else if (fragment instanceof AttendanceFragment) {
            switchFragment(INT_FRAGMENT_MORE_POS, TAG_FRAG_MORE);
        } else if (fragment instanceof FeedbackFragment) {
            switchFragment(INT_FRAGMENT_MORE_POS, TAG_FRAG_MORE);
        } else {
            switchFragment(INT_FRAGMENT_CHECK_IN_POS, TAG_FRAG_CHECK_IN);
        }

    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_HOME_ACTIVITY, "instantiateViews() has been instantiated");

        bnvNavigation.getMenu().getItem(INT_FRAGMENT_CHECK_IN_POS).setChecked(true);
        bnvNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(bnvNavigation);

        buildFragmentsList();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_fragment_holder, CheckInFragment.newInstance()).commit();
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
//                item.setShiftingMode(false);
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
}
