package com.example.moham.soleeklab.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Interfaces.VerifyIdentityInterface;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.Constants;
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
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;
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

    private String mailExtra = null;
    private String verificationCode = null;
    CountDownTimer timer = null;
    private Employee currentEmployee;

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

        if (!isNetworkAvailable()) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "No Network Connection");

            showNoNetworkDialog();
            return;
        }

        if (!TextUtils.isEmpty(verificationCode)) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Verifying Email address");

//            final AlertDialog.Builder showLoadingDialog = new AlertDialog.Builder(getContext());
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            View view = inflater.inflate(R.layout.loading, null);
//            showLoadingDialog.setView(view);
//
//            try {
//                GifImageView gifImageView = view.findViewById(R.id.gif_loading);
//                GifDrawable gifFromAssets = new GifDrawable(getActivity().getAssets(), "logoloading.gif");
//                gifImageView.setImageDrawable(gifFromAssets);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//            final AlertDialog dialog = showLoadingDialog.create();
//
//            dialog.show();
//            dialog.getWindow().setAttributes(layoutParams);
//            dialog.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            tvErrorMessage.setVisibility(View.GONE);
            startActivity(new Intent(getContext(), LoadingActivity.class));

            if (verificationCode != null && mailExtra != null) {

                ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
                Call<Employee> call = service.verifyRestCode(mailExtra, Integer.parseInt(verificationCode));

                call.enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG_FRAG_FORGET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                            currentEmployee = response.body();
                            if (currentEmployee.getError() == null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("extra_verification_code", verificationCode);
                                bundle.putString("extra_verification_email", mailExtra);
                                ResettingPasswordFragment fragment = new ResettingPasswordFragment();
                                fragment.setArguments(bundle);
                                replaceFragmentWithAnimation(fragment, TAG_FRAG_RESET_PASS);
                                getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                            } else {
                                getResponseErrorMessage(getActivity(), response);
                            }
                        } else {
                            getResponseErrorMessage(getActivity(), response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {
                        Log.e(TAG_FRAG_VERIFY_IDENTITY, "onFailure(): " + t.toString());
                        Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                        getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    }
                });


            }
        }

    }

    @OnClick(R.id.ib_verify_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "back::ImageView has been clicked");
        clearBackStack();
        replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "replaceFragmentWithAnimation() has been instantiated");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
//                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public boolean isNetworkAvailable() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "isNetworkAvailable() has been instantiated");
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showNoNetworkDialog() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "showNoNetworkDialog() has been instantiated");

        final AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.no_internet_dialog, null);
        noNetworkDialog.setView(view);

        final AlertDialog dialog = noNetworkDialog.create();
        dialog.show();
        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        Button btnDone = view.findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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

        if (isNetworkAvailable()) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Resending verification code");

            tvErrorMessage.setVisibility(View.GONE);
            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            Call<Employee> call = service.sendEmailToResetPassword(mailExtra);

            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_FORGET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                        currentEmployee = response.body();
                        if (currentEmployee.getError() == null) {
                            Toast.makeText(getContext(), "Check Your Mail", Toast.LENGTH_SHORT).show();
                        } else {
                            getResponseErrorMessage(getActivity(), response);
                        }
                    } else {
                        getResponseErrorMessage(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
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
    public void clearBackStack() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "clearBackStack() has been instantiated");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager != null) {
            Constants.sDisableFragmentAnimations = true;
            manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Constants.sDisableFragmentAnimations = false;
        } else
            Log.w(TAG_FRAG_VERIFY_IDENTITY, "FragmentManager -> null");
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (Constants.sDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
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
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "getResponseErrorMessage() has been instantiated");
        if (response.code() == 401 || response.code() == 400) {
            Log.d(TAG_FRAG_VERIFY_IDENTITY, "Response code ------> " + response.code() + " " + response.message());
            try {
                Gson gson = new Gson();
                Employee errorModel = gson.fromJson(response.errorBody().string(), Employee.class);
                if (errorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_VERIFY_IDENTITY, "getResponseErrorMessage() errorModel.Message = " + errorModel.getMessage());
                    getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(errorModel.getMessage());
                } else {
                    Log.i(TAG_FRAG_VERIFY_IDENTITY, "getResponseErrorMessage() errorModel.Message = SOMETHING_WENT_WRONG");
                    getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.code() == 500) {
            getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
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
}
