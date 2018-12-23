package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Interfaces.CheckInFragmentInterface;
import com.example.moham.soleeklab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.FONT_LIBREFRANKLIN_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_CHECK_IN_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_CHECK_IN;

public class CheckInFragment extends Fragment implements CheckInFragmentInterface {

    @BindView(R.id.iv_user_profile_pic)
    ImageView ivUserProfilePic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_job_title)
    TextView tvUserJobTitle;
    @BindView(R.id.tv_check_in_text)
    TextView tvCheckInText;
    Unbinder unbinder;

    HomeActivity mHomeActivity;

    public CheckInFragment() {
    }

    public static CheckInFragment newInstance() {
        return new CheckInFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_CHECK_IN, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);
        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_CHECK_IN_POS).setChecked(true);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_CHECK_IN, "replaceFragmentWithAnimation() has been instantiated");
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_CHECK_IN, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_CHECK_IN, "setFontsToViews() has been instantiated");
        tvUserName.setTypeface(loadFont(getContext(), FONT_DOSIS_MEDIUM));
        tvUserJobTitle.setTypeface(loadFont(getContext(), FONT_DOSIS_MEDIUM));
        tvCheckInText.setTypeface(loadFont(getContext(), FONT_LIBREFRANKLIN_MEDIUM));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_CHECK_IN, "onDestroyView() has been instantiated");

        unbinder.unbind();
    }


    @OnClick(R.id.cl_user_status_login)
    @Override
    public void handleCheckIn() {
        Log.d(TAG_FRAG_CHECK_IN, "handleCheckIn() has been instantiated");

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_CHECK_IN, "onAttach() has been instantiated");
        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }
}
