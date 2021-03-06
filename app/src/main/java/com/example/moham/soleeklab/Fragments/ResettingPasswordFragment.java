package com.example.moham.soleeklab.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Interfaces.ResettingPassInterface;
import com.example.moham.soleeklab.Model.Responses.EmployeeResponse;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_REGULAR;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_SEMI_BOLD;
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.INT_NEW_PASS;
import static com.example.moham.soleeklab.Utils.Constants.INT_RETYPED_PASS;
import static com.example.moham.soleeklab.Utils.Constants.LOGIN_PASS_MIN_LENGTH;
import static com.example.moham.soleeklab.Utils.Constants.STR_EXTRA_CODE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_RESET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_RESET_PASS_REC;

public class ResettingPasswordFragment extends Fragment implements ResettingPassInterface {

    @BindView(R.id.new_password)
    EditText edtNewPassword;
    @BindView(R.id.input_new_password_layout)
    TextInputLayout tilNewPasswordLayout;
    @BindView(R.id.retype_new_password)
    EditText edtRetypeNewPassword;
    @BindView(R.id.input_retype_new_password_layout)
    TextInputLayout tilRetypeNewPasswordLayout;
    @BindView(R.id.ib_back)
    ImageView ivBack;
    @BindView(R.id.reset)
    Button btnReset;
    Unbinder unbinder;
    @BindView(R.id.tv_resetting_pass)
    TextView tvResettingPass;
    @BindView(R.id.error_message)
    TextView tvErrorMessage;

    private String extraVerificationCode;
    private String extraEmail;
    private EmployeeResponse currentEmployeeResponse;
    private Call<EmployeeResponse> ResetPassRequestCall;
    private ResetPassReceiver mResetPassReceiver;

    public ResettingPasswordFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_FRAG_RESET_PASS, "onCreate() has been instantiated");

        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CANCEL_RESET_PASS);
        mResetPassReceiver = new ResetPassReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mResetPassReceiver, filter);
    }

    public static ResettingPasswordFragment newInstance() {
        Log.d(TAG_FRAG_RESET_PASS, "newInstance() has been instantiated");
        return new ResettingPasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_RESET_PASS, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_resetting_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        instantiateViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_RESET_PASS, "onDestroyView() has been instantiated");

        unbinder.unbind();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mResetPassReceiver);
    }

    @OnClick(R.id.ib_back)
    public void handleBackClicked() {
        Log.d(TAG_FRAG_RESET_PASS, "back::ImageView has been clicked");
        replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
    }

    @Override
    public boolean checkPasswordValidation(String password, int whichPassword) {
        Log.d(TAG_FRAG_RESET_PASS, "checkPasswordValidation() has been instantiated");

        if (whichPassword == INT_NEW_PASS) {
            int passLength = edtNewPassword.getText().toString().length();

            if (passLength >= LOGIN_PASS_MIN_LENGTH) {
                tilNewPasswordLayout.setError(null);
                return true;
            } else {
                tilNewPasswordLayout.setError(getString(R.string.error_short_password));
                return false;
            }
        } else if (whichPassword == INT_RETYPED_PASS) {
            int passLength = edtRetypeNewPassword.getText().toString().length();

            if (passLength >= LOGIN_PASS_MIN_LENGTH) {
                tilRetypeNewPasswordLayout.setError(null);
                return true;
            } else {
                tilRetypeNewPasswordLayout.setError(getString(R.string.error_short_password));
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean checkPasswordMatch(String password, String reTypedPassword) {
        Log.d(TAG_FRAG_RESET_PASS, "checkPasswordMatch() has been instantiated");

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(reTypedPassword) && password.equals(reTypedPassword)) {
            tilRetypeNewPasswordLayout.setError(null);
            return true;
        }
        tilRetypeNewPasswordLayout.setError(getString(R.string.error_match_password));
        return false;
    }

    @OnClick(R.id.reset)
    @Override
    public void resetPassword() {
        Log.d(TAG_FRAG_RESET_PASS, "btnReset::Button has been clicked");

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_RESET_PASS, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        String pass = edtNewPassword.getText().toString();
        String retypedPass = edtRetypeNewPassword.getText().toString();

        if (checkPasswordValidation(pass, INT_NEW_PASS) && checkPasswordValidation(retypedPass, INT_RETYPED_PASS) && checkPasswordMatch(pass, retypedPass)) {
            Log.d(TAG_FRAG_RESET_PASS, "Resetting user password");

            tvErrorMessage.setVisibility(View.GONE);

            Bundle bundle = new Bundle();
            bundle.putInt(STR_EXTRA_CODE, INT_CANCEL_RESET_PASS);
            Intent intent = new Intent(getContext(), LoadingActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            ResetPassRequestCall = service.resetUserPassword(extraEmail, pass, extraVerificationCode);

            ResetPassRequestCall.enqueue(new Callback<EmployeeResponse>() {
                @Override
                public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_RESET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                        currentEmployeeResponse = response.body();
                        if (currentEmployeeResponse.getError() == null) {
                            replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                            Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
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

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_RESET_PASS, "instantiateViews() has been instantiated");

        setFontsToViews();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        btnReset.setText(getString(R.string.reset));
        btnReset.setBackgroundResource(R.drawable.button_gray);
        btnReset.setEnabled(false);

        Bundle extraCode = this.getArguments();
        if (extraCode != null) {
            extraVerificationCode = extraCode.getString("extra_verification_code");
            extraEmail = extraCode.getString("extra_verification_email");
            Log.d(TAG_FRAG_RESET_PASS, "Bundle extra verification code -> " + extraVerificationCode);
            Log.d(TAG_FRAG_RESET_PASS, "Bundle extra email -> " + extraEmail);
        } else {
            Log.d(TAG_FRAG_RESET_PASS, "Bundle extra verification code -> NULL");
        }

        TextWatcher mPassTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = edtNewPassword.getText().toString();

                if (!TextUtils.isEmpty(pass)) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorBlue));
                    ViewCompat.setBackgroundTintList(edtNewPassword, colorStateList);
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorGray));
                    ViewCompat.setBackgroundTintList(edtNewPassword, colorStateList);
                }
            }
        };

        TextWatcher mRetypedPassTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = edtRetypeNewPassword.getText().toString();

                if (!TextUtils.isEmpty(pass)) {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorBlue));
                    ViewCompat.setBackgroundTintList(edtRetypeNewPassword, colorStateList);
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorGray));
                    ViewCompat.setBackgroundTintList(edtRetypeNewPassword, colorStateList);
                }
            }
        };

        TextWatcher mEnableBtnTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = edtNewPassword.getText().toString();
                String reTypedPass = edtRetypeNewPassword.getText().toString();

                if (!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(reTypedPass)) {
                    btnReset.setEnabled(true);
                    btnReset.setBackgroundResource(R.drawable.button_green);
                } else {
                    btnReset.setEnabled(false);
                    btnReset.setBackgroundResource(R.drawable.button_gray);
                }
            }
        };

        edtNewPassword.addTextChangedListener(mPassTextWatcher);
        edtRetypeNewPassword.addTextChangedListener(mRetypedPassTextWatcher);
        edtNewPassword.addTextChangedListener(mEnableBtnTextWatcher);
        edtRetypeNewPassword.addTextChangedListener(mEnableBtnTextWatcher);
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_RESET_PASS, "replaceFragmentWithAnimation() has been instantiated");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.commit();
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_RESET_PASS, "loadFont() has been instantiated");
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_RESET_PASS, "setFontsToViews() has been instantiated");
        tvResettingPass.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tilNewPasswordLayout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tilRetypeNewPasswordLayout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        edtNewPassword.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        edtRetypeNewPassword.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        btnReset.setTypeface(loadFont(getContext(), FONT_DOSIS_SEMI_BOLD));
    }

    @Override
    public void getResponseErrorMessage(Context context, Response response) {
        Log.d(TAG_FRAG_RESET_PASS, "getLoginResponseErrorMessage() has been instantiated");
        if (response.code() == 401 || response.code() == 400) {
            Log.d(TAG_FRAG_RESET_PASS, "Response code ------> " + response.code() + " " + response.message());
            try {
                Gson gson = new Gson();
                EmployeeResponse errorModel = gson.fromJson(response.errorBody().string(), EmployeeResponse.class);
                if (errorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_RESET_PASS, "getLoginResponseErrorMessage() errorModel.Message = " + errorModel.getMessage());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(errorModel.getMessage());
                } else {
                    Log.i(TAG_FRAG_RESET_PASS, "getLoginResponseErrorMessage() errorModel.Message = SOMETHING_WENT_WRONG");
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

    class ResetPassReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_RESET_PASS_REC, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CANCEL_RESET_PASS)) {
                Log.d(TAG_RESET_PASS_REC, "cancelling reset password request");
                ResetPassRequestCall.cancel();
            }
        }
    }
}
