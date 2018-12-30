package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Interfaces.CheckInFragmentInterface;
import com.example.moham.soleeklab.Model.Responses.CheckInResponse;
import com.example.moham.soleeklab.Model.Responses.EmployeeResponse;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.FONT_LIBREFRANKLIN_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_CHECK_IN_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_CHECK_IN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_HOME;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

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
    @BindView(R.id.v_circle)
    View vCircle;
    @BindView(R.id.cl_user_status_checkout)
    ConstraintLayout clUserStatusLogin;
    HomeActivity mHomeActivity;
    private HeaderInjector headerInjector;

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
        instantiateViews();

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


    @OnClick(R.id.v_circle)
    @Override
    public void handleCheckIn() {
        Log.d(TAG_FRAG_CHECK_IN, "handleCheckIn() has been instantiated");

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_CHECK_IN, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        Log.d(TAG_FRAG_CHECK_IN, "Showing loading activity");
        startActivity(new Intent(getContext(), LoadingActivity.class));

        Log.e(TAG_FRAG_CHECK_IN, "Checking employee in");
        ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
        Call<CheckInResponse> call = service.checkInUser(headerInjector.getHeaders());
        call.enqueue(new Callback<CheckInResponse>() {
            @Override
            public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                if (response.isSuccessful()) {

                    Log.e(TAG_FRAG_CHECK_IN, "Response code -> " + response.code() + " " + response.message() + " ");
                    Log.d(TAG_FRAG_CHECK_IN, "Saving CheckInResponse object to preferences");
                    EmployeeSharedPreferences.SaveCheckInResponseToPreferences(getActivity(), response.body());

                    // Navigate to HomeFragment, Show everything
                    Log.d(TAG_FRAG_CHECK_IN, "Navigating to the HomeFragment");
                    switchFragment(HomeFragment.newInstance(), TAG_FRAG_HOME);
                }
            }

            @Override
            public void onFailure(Call<CheckInResponse> call, Throwable t) {
                Log.e(TAG_FRAG_CHECK_IN, "onFailure(): " + t.toString());
//                getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_CHECK_IN, "onAttach() has been instantiated");
        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }

    @Override
    public void switchFragment(Fragment fragment, final String tag) {
        Log.d(TAG_FRAG_CHECK_IN, "switchFragment() has been instantiated");
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_CHECK_IN, "instantiateViews() has been instantiated");
        setFontsToViews();
        headerInjector = new HeaderInjectorImplementation(getActivity());

        Log.d(TAG_FRAG_CHECK_IN, "Loading CheckInResponse from the preferences");
        EmployeeResponse curEmp = EmployeeSharedPreferences.readEmployeeFromPreferences(getActivity());

        String empJobTitle = curEmp.getUser().getJobTitle();
        Log.d(TAG_FRAG_CHECK_IN, "empJobTitle ------>" + empJobTitle);
        tvUserJobTitle.setText(empJobTitle);

        String empName = curEmp.getUser().getName();
        Log.d(TAG_FRAG_CHECK_IN, "empName ------>" + empName);
        tvUserName.setText(empName);

        String empProfilePicStr = curEmp.getUser().getProfilePic();
        Log.d(TAG_FRAG_CHECK_IN, "empProfilePicStr ------>" + empProfilePicStr);
        Glide.with(this).load(empProfilePicStr).apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL)).into(ivUserProfilePic);
    }

}
