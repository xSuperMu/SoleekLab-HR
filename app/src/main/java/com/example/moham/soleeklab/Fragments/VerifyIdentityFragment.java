package com.example.moham.soleeklab.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Interfaces.VerifyIdentityInterface;
import com.example.moham.soleeklab.Model.Responses.EmployeeResponse;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_REGULAR;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_SEMI_BOLD;
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.STR_EXTRA_CODE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_VERIFY_ID_REC;
import static com.example.moham.soleeklab.Utils.Constants.TOTAL_NUMBER_OF_ITEMS;

public class VerifyIdentityFragment extends Fragment implements VerifyIdentityInterface {

    @BindView(R.id.pinView)
    PinView pinView;
    Unbinder unbinder;
    @BindView(R.id.verify_email_message)
    TextView tvVerifyEmailMessage;
    @BindView(R.id.cv_background_1)
    CardView cvBackground1;
    @BindView(R.id.cv_background_2)
    CardView cvBackground2;
    @BindView(R.id.cv_background_3)
    CardView cvBackground3;
    @BindView(R.id.cv_background_4)
    CardView cvBackground4;
    @BindView(R.id.btn_verify_identity)
    Button btnVerifyIdentity;
    @BindView(R.id.ib_verify_back)
    ImageView ibBack;
    @BindView(R.id.error_message)
    TextView tvErrorMessage;
    @BindView(R.id.tv_resend)
    TextView tvResend;
    @BindView(R.id.ib_reload)
    ImageView ibReload;
    @BindView(R.id.tv_verify_identity)
    TextView tvVerifyIdentity;
    CountDownTimer timer = null;
    private String mailExtra = null;
    private String verificationCode = null;
    private EmployeeResponse currentEmployeeResponse;

    private Call<EmployeeResponse> verifyIdentityRequestCall;
    private VerifyIdentityReceiver mVerifyIdentityReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "onCreate() has been instantiated");

        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CANCEL_VERIFY_IDENTITY);
        mVerifyIdentityReceiver = new VerifyIdentityReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mVerifyIdentityReceiver, filter);
    }

    public VerifyIdentityFragment() {
    }

    public static VerifyIdentityFragment newInstance() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "newInstance() has been instantiated");
        return new VerifyIdentityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "onCreateView() has been instantiated");

        View view = inflater.inflate(R.layout.fragment_verify_identity, container, false);
        unbinder = ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        instantiateViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "onDestroyView() has been instantiated");

        unbinder.unbind();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (timer != null) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "onDestroy: Cancelling Timer");
            timer.cancel();
            timer = null;
        }

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mVerifyIdentityReceiver);

        Log.d(TAG_FRAG_VERIFY_IDENTITY, "onDestroyView() has finished");
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "instantiateViews() has been instantiated");

        setFontsToViews();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        startTimer();

        btnVerifyIdentity.setEnabled(false);
        btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
        btnVerifyIdentity.setText(getString(R.string.verify));

        Bundle extraEmail = this.getArguments();
        if (extraEmail != null) {
            mailExtra = extraEmail.getString("extra_email", "abc@soleek.com");
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Bundle extra email -> " + mailExtra);
            String str = getResources().getString(R.string.email_messages, mailExtra);
            tvVerifyEmailMessage.setText(str);
        } else {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Bundle extra email -> NULL");
        }

        pinView.setItemRadius(10);
        pinView.setLineWidth(2);
        pinView.setAnimationEnable(true);
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                switch (length) {
                    case 1:
                        cvBackground1.setVisibility(View.VISIBLE);
                        cvBackground2.setVisibility(View.INVISIBLE);
                        cvBackground3.setVisibility(View.INVISIBLE);
                        cvBackground4.setVisibility(View.INVISIBLE);

                        btnVerifyIdentity.setEnabled(false);
                        btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
                        break;
                    case 2:
                        cvBackground1.setVisibility(View.VISIBLE);
                        cvBackground2.setVisibility(View.VISIBLE);
                        cvBackground3.setVisibility(View.INVISIBLE);
                        cvBackground4.setVisibility(View.INVISIBLE);
                        btnVerifyIdentity.setEnabled(false);
                        btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
                        break;
                    case 3:
                        cvBackground1.setVisibility(View.VISIBLE);
                        cvBackground2.setVisibility(View.VISIBLE);
                        cvBackground3.setVisibility(View.VISIBLE);
                        cvBackground4.setVisibility(View.INVISIBLE);
                        btnVerifyIdentity.setEnabled(false);
                        btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
                        break;
                    case 4:
                        cvBackground1.setVisibility(View.VISIBLE);
                        cvBackground2.setVisibility(View.VISIBLE);
                        cvBackground3.setVisibility(View.VISIBLE);
                        cvBackground4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        cvBackground1.setVisibility(View.INVISIBLE);
                        cvBackground2.setVisibility(View.INVISIBLE);
                        cvBackground3.setVisibility(View.INVISIBLE);
                        cvBackground4.setVisibility(View.INVISIBLE);
                        btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
                }

                if (length == TOTAL_NUMBER_OF_ITEMS) {
                    Log.d(TAG_FRAG_VERIFY_IDENTITY, "verified code: " + s.toString());
                    verificationCode = s.toString();
                    btnVerifyIdentity.setEnabled(true);
                    btnVerifyIdentity.setBackgroundResource(R.drawable.button_green);

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } else {
                    btnVerifyIdentity.setEnabled(false);
                    btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
                }
            }
        });
    }

    @OnClick(R.id.btn_verify_identity)
    @Override
    public void verifyUser() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "verifyUser() has been instantiated");

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        if (!TextUtils.isEmpty(verificationCode)) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Verifying Email address");

            tvErrorMessage.setVisibility(View.GONE);

            Bundle bundle = new Bundle();
            bundle.putInt(STR_EXTRA_CODE, INT_CANCEL_VERIFY_IDENTITY);
            Intent intent = new Intent(getContext(), LoadingActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

            if (verificationCode != null && mailExtra != null) {

                ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
                verifyIdentityRequestCall = service.verifyRestCode(mailExtra, Integer.parseInt(verificationCode));

                verifyIdentityRequestCall.enqueue(new Callback<EmployeeResponse>() {
                    @Override
                    public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG_FRAG_FORGET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                            currentEmployeeResponse = response.body();
                            if (currentEmployeeResponse.getError() == null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("extra_verification_code", verificationCode);
                                bundle.putString("extra_verification_email", mailExtra);
                                ResettingPasswordFragment fragment = new ResettingPasswordFragment();
                                fragment.setArguments(bundle);
                                replaceFragmentWithAnimation(fragment, TAG_FRAG_RESET_PASS);
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                            } else {
                                getResponseErrorMessage(getActivity(), response);
                            }
                        } else {
                            getResponseErrorMessage(getActivity(), response);
                        }
                    }

                    @Override
                    public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                        Log.e(TAG_FRAG_VERIFY_IDENTITY, "onFailure(): " + t.toString());
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                        if (call.isCanceled())
                            Toast.makeText(getActivity(), "Canceled!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @OnClick(R.id.ib_verify_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "back::ImageView has been clicked");
        replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "replaceFragmentWithAnimation() has been instantiated");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.tv_resend, R.id.ib_reload})
    public void handleResend() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "handleResend() has been instantiated");

        startTimer();

        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvResend.setText("" + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                enableViews();
            }
        }.start();

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Resending verification code");

            tvErrorMessage.setVisibility(View.GONE);
            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            Call<EmployeeResponse> mResendEmailRequestCall = service.sendEmailToResetPassword(mailExtra);

            mResendEmailRequestCall.enqueue(new Callback<EmployeeResponse>() {
                @Override
                public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_FORGET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                        currentEmployeeResponse = response.body();
                        if (currentEmployeeResponse.getError() == null) {
                            Toast.makeText(getContext(), "Check Your Mail", Toast.LENGTH_SHORT).show();
                        } else {
                            getResponseErrorMessage(getActivity(), response);
                        }
                    } else {
                        getResponseErrorMessage(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<EmployeeResponse> call, Throwable t) {
                    Log.e(TAG_FRAG_VERIFY_IDENTITY, "onFailure(): " + t.toString());
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            timer.cancel();
            enableViews();
        }

    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "loadFont() has been instantiated");
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "setFontsToViews() has been instantiated");
        tvVerifyIdentity.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tvVerifyEmailMessage.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        pinView.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tvResend.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        btnVerifyIdentity.setTypeface(loadFont(getContext(), FONT_DOSIS_SEMI_BOLD));
    }

    @Override
    public void getResponseErrorMessage(Context context, Response response) {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "getLoginResponseErrorMessage() has been instantiated");
        if (response.code() == 401 || response.code() == 400) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Response code ------> " + response.code() + " " + response.message());
            try {
                Gson gson = new Gson();
                EmployeeResponse errorModel = gson.fromJson(response.errorBody().string(), EmployeeResponse.class);
                if (errorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_VERIFY_IDENTITY, "getLoginResponseErrorMessage() errorModel.Message = " + errorModel.getMessage());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(errorModel.getMessage());
                } else {
                    Log.i(TAG_FRAG_VERIFY_IDENTITY, "getLoginResponseErrorMessage() errorModel.Message = SOMETHING_WENT_WRONG");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.code() == 500) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void enableViews() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "enableViews() has been instantiated");

        ibReload.setEnabled(true);
        ibReload.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_black_24dp));
        tvResend.setEnabled(true);
        tvResend.setTextColor(getResources().getColor(R.color.colorPurple));
        tvResend.setText(getResources().getString(R.string.resend));
    }

    @Override
    public void disableViews() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "disableViews() has been instantiated");
        ibReload.setEnabled(false);
        ibReload.setImageDrawable(getResources().getDrawable(R.drawable.ic_refresh_gray_24dp));
        tvResend.setEnabled(false);
        tvResend.setTextColor(getResources().getColor(R.color.colorGray));
    }

    public void startTimer() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "startTimer() has been instantiated");

        disableViews();

        if (timer != null)
            timer.cancel();
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (timer != null)
                    tvResend.setText("" + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                enableViews();
            }
        }.start();
    }

    class VerifyIdentityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_VERIFY_ID_REC, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CANCEL_VERIFY_IDENTITY)) {
                Log.d(TAG_VERIFY_ID_REC, "cancelling verify identity request");
                verifyIdentityRequestCall.cancel();
            }
        }
    }
}
