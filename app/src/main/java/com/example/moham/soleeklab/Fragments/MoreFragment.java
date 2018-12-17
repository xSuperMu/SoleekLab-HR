package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moham.soleeklab.Activities.AuthActivity;
import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Interfaces.MoreFragmentInterface;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_REGULAR;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_SEMI_BOLD;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_MORE_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_ATTENDANCE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FEEDBACK;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_MORE;

public class MoreFragment extends Fragment implements MoreFragmentInterface {

    @BindView(R.id.tv_action_bar_more)
    TextView tvActionBarMore;
    @BindView(R.id.ll_attendance)
    LinearLayout llAttendance;
    @BindView(R.id.ll_feedback)
    LinearLayout llFeedback;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;
    Unbinder unbinder;
    HomeActivity homeActivity;

    public MoreFragment() {
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_MORE, "onAttach() has been instantiated");

        if (context instanceof Activity) {
            homeActivity = (HomeActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_MORE, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, view);
        homeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_MORE_POS - 1).setChecked(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_MORE, "onDestroyView() has been instantiated");

        unbinder.unbind();
    }

    @OnClick(R.id.ll_attendance)
    @Override
    public void handleAttendanceClick() {
        Log.d(TAG_FRAG_MORE, "Attendance::Button has been clicked");
        switchFragment(AttendanceFragment.newInstance(), TAG_FRAG_ATTENDANCE);
    }

    @OnClick(R.id.ll_feedback)
    @Override
    public void handleFeedbackClick() {
        Log.d(TAG_FRAG_MORE, "Feedback::Button has been clicked");
        switchFragment(FeedbackFragment.newInstance(), TAG_FRAG_FEEDBACK);

    }

    @OnClick(R.id.ll_logout)
    @Override
    public void handleLogoutClick() {
        Log.d(TAG_FRAG_MORE, "Logout::Button has been clicked");
        // TODO(1) Clear User Credentials
        showConfirmLogoutDialog();
    }

    @Override
    public void switchFragment(Fragment fragment, final String tag) {
        Log.d(TAG_FRAG_MORE, "switchFragment() has been instantiated");
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void showConfirmLogoutDialog() {
        Log.d(TAG_FRAG_MORE, "showConfirmLogoutDialog() has been instantiated");
        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.logout_dialog, null);
        noNetworkDialog.setView(view);

        final AlertDialog dialog = noNetworkDialog.create();
        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        TextView infoLogout = view.findViewById(R.id.tv_logout_info);
        TextView extraInfoLogout = view.findViewById(R.id.tv_logout_more_info);
        Button btnLogout = view.findViewById(R.id.btn_logout);
        Button btnStay = view.findViewById(R.id.btn_stay);
        infoLogout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        extraInfoLogout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        btnLogout.setTypeface(loadFont(getContext(), FONT_DOSIS_SEMI_BOLD));
        btnStay.setTypeface(loadFont(getContext(), FONT_DOSIS_SEMI_BOLD));
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG_FRAG_MORE, "Logout::Button has been clicked");
                Log.d(TAG_FRAG_MORE, "Clearing user credentials");
                EmployeeSharedPreferences.clearPreferences(getActivity());
                Intent authIntent = new Intent(getActivity(), AuthActivity.class);
                getActivity().startActivity(authIntent);
                getActivity().finish();
                getActivity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        btnStay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG_FRAG_MORE, "Stay::Button has been clicked");
                dialog.dismiss();
            }
        });
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_MORE, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }
}
