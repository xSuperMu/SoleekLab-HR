package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
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

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Adapter.NotificationAdapter;
import com.example.moham.soleeklab.Interfaces.NotificationFragInterface;
import com.example.moham.soleeklab.Model.Notification;
import com.example.moham.soleeklab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_NOTIFICATIONS_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NOTIFICATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_TASKS;

public class NotificationFragment extends Fragment implements NotificationFragInterface, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_action_bar_notification)
    TextView tvActionBarNotification;
    @BindView(R.id.iv_no_notification_icon)
    ImageView ivNoNotificationIcon;
    @BindView(R.id.tv_no_notification_text)
    TextView tvNoNotificationText;
    @BindView(R.id.cl_no_notification)
    ConstraintLayout clNoNotification;
    @BindView(R.id.rv_notification)
    RecyclerView rvNotification;
    Unbinder unbinder;
    @BindView(R.id.srlNotificationSwipe)
    SwipeRefreshLayout srlNotificationSwipe;
    NotificationAdapter mNotificationAdapter;
    private List<Notification> mUserNotifications;
    HomeActivity mHomeActivity;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_NOTIFICATION, "onCreateView() has been instantiated");
        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_NOTIFICATIONS_POS - 1).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_NOTIFICATION, "onDestroyView() has been instantiated");

        unbinder.unbind();
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_NOTIFICATION, "instantiateViews() has been instantiated");


        // RecyclerView object dependencies
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNotification.setLayoutManager(mLinearLayoutManager);
        rvNotification.setHasFixedSize(true);

        mNotificationAdapter = new NotificationAdapter(getActivity());
        rvNotification.setAdapter(mNotificationAdapter);

        srlNotificationSwipe.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        srlNotificationSwipe.post(new Runnable() {

            @Override
            public void run() {
                srlNotificationSwipe.setRefreshing(true);
                // Fetching data from server
            }
        });
    }

    @Override
    public void loadNotificationData() {
        Log.d(TAG_FRAG_NOTIFICATION, "loadNotificationData() has been instantiated");
    }

    @Override
    public void onRefresh() {
        Log.d(TAG_FRAG_NOTIFICATION, "onRefresh() has been instantiated");
        // refresh the data
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_TASKS, "onAttach() has been instantiated");

        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }
}
