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
import android.util.Patterns;
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
import com.example.moham.soleeklab.Interfaces.AuthForgetPasswordInterface;
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
import static com.example.moham.soleeklab.Utils.Constants.INT_CANCEL_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.STR_EXTRA_CODE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FOR_PASS_REC;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

public class ForgetPasswordFragment extends Fragment implements AuthForgetPasswordInterface {

    @BindView(R.id.ib_back)
    ImageView ibBack;
    @BindView(R.id.forget_email)
    EditText edtForgetEmail;
    @BindView(R.id.input_forget_email_layout)
    TextInputLayout tilInputForgetEmailLayout;
    @BindView(R.id.btn_send_email)
    Button btnSendEmail;
    Unbinder unbinder;
    @BindView(R.id.tv_verify_identity)
    TextView tvForgetPass;
    @BindView(R.id.forget_password_message)
    TextView tvForgetPasswordMessage;
    @BindView(R.id.error_message)
    TextView tvErrorMessage;
    Call<EmployeeResponse> sendEmailRequestCall;
    private EmployeeResponse currentEmployeeResponse;
    private ForgetPassReceiver mForgetPassReceiver;

    public ForgetPasswordFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_FRAG_FORGET_PASS, "onCreate() has been instantiated");
        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CANCEL_FORGET_PASS);
        mForgetPassReceiver = new ForgetPassReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mForgetPassReceiver, filter);
    }

    public static ForgetPasswordFragment newInstance() {
        Log.d(TAG_FRAG_FORGET_PASS, "newInstance() has been instantiated");
        return new ForgetPasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_FORGET_PASS, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG_FRAG_FORGET_PASS, "onDestroyView() has been instantiated");
        super.onDestroyView();
        unbinder.unbind();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mForgetPassReceiver);
    }

    @OnClick(R.id.ib_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "back::ImageView has been clicked");
        replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
    }

    @OnClick(R.id.btn_send_email)
    public void onBtnSendEmailClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "onBtnSendEmailClicked() has been instantiated");

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_FORGET_PASS, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        final String email = edtForgetEmail.getText().toString();
        if (checkEmailValidation(email) && NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_FORGET_PASS, "Valid Email address");
            tvErrorMessage.setVisibility(View.GONE);

            Bundle bundle = new Bundle();
            bundle.putInt(STR_EXTRA_CODE, INT_CANCEL_FORGET_PASS);
            Intent intent = new Intent(getContext(), LoadingActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            sendEmailRequestCall = service.sendEmailToResetPassword(email);

            sendEmailRequestCall.enqueue(new Callback<EmployeeResponse>() {
                @Override
                public void onResponse(Call<EmployeeResponse> call, Response<EmployeeResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_FORGET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                        currentEmployeeResponse = response.body();
                        if (currentEmployeeResponse.getError() == null) {
                            Bundle extraEmail = new Bundle();
                            extraEmail.putString("extra_email", email);
                            VerifyIdentityFragment fragment = new VerifyIdentityFragment();
                            fragment.setArguments(extraEmail);
                            replaceFragmentWithAnimation(fragment, TAG_FRAG_VERIFY_IDENTITY);
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
                    Log.e(TAG_FRAG_FORGET_PASS, "onFailure(): " + t.toString());
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
    public boolean checkEmailValidation(String email) {
        Log.d(TAG_FRAG_FORGET_PASS, "checkEmailValidation() has been instantiated");
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilInputForgetEmailLayout.setError(null);
            return true;
        } else {
            tilInputForgetEmailLayout.setError(getString(R.string.error_invalid_email));
            return false;
        }
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_FORGET_PASS, "instantiateViews() has been instantiated");
        setFontsToViews();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        btnSendEmail.setText(getString(R.string.send_email));
        btnSendEmail.setBackgroundResource(R.drawable.button_gray);
        btnSendEmail.setEnabled(false);

        TextWatcher mEmailTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = edtForgetEmail.getText().toString();

                if (!TextUtils.isEmpty(email)) {
                    btnSendEmail.setEnabled(true);
                    btnSendEmail.setBackgroundResource(R.drawable.button_green);

                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorBlue));
                    ViewCompat.setBackgroundTintList(edtForgetEmail, colorStateList);
                } else {
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorGray));
                    ViewCompat.setBackgroundTintList(edtForgetEmail, colorStateList);

                    btnSendEmail.setEnabled(false);
                    btnSendEmail.setBackgroundResource(R.drawable.button_gray);
                }
            }
        };
        edtForgetEmail.addTextChangedListener(mEmailTextWatcher);
    }


    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_FORGET_PASS, "replaceFragmentWithAnimation() has been instantiated");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_FORGET_PASS, "loadFont() has been instantiated");
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_FORGET_PASS, "setFontsToViews() has been instantiated");
        tvForgetPass.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tvForgetPasswordMessage.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tilInputForgetEmailLayout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        edtForgetEmail.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        btnSendEmail.setTypeface(loadFont(getContext(), FONT_DOSIS_SEMI_BOLD));
    }

    @Override
    public void getResponseErrorMessage(Context context, Response response) {
        Log.d(TAG_FRAG_FORGET_PASS, "getLoginResponseErrorMessage() has been instantiated");
        if (response.code() == 401 || response.code() == 400) {
            Log.d(TAG_FRAG_FORGET_PASS, "Response code ------> " + response.code() + " " + response.message());
            try {
                Gson gson = new Gson();
                EmployeeResponse errorModel = gson.fromJson(response.errorBody().string(), EmployeeResponse.class);
                if (errorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_FORGET_PASS, "getLoginResponseErrorMessage() errorModel.Message = " + errorModel.getMessage());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(errorModel.getMessage());
                } else {
                    Log.i(TAG_FRAG_FORGET_PASS, "getLoginResponseErrorMessage() errorModel.Message = SOMETHING_WENT_WRONG");
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

    class ForgetPassReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_FOR_PASS_REC, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CANCEL_FORGET_PASS)) {
                Log.d(TAG_FOR_PASS_REC, "cancelling forget pass request");
                sendEmailRequestCall.cancel();
            }
        }
    }
}