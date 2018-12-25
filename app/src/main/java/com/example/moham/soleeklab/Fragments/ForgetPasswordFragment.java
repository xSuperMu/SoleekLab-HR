package com.example.moham.soleeklab.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Network.ClientService;
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
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;

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

    private Employee currentEmployee;

    public ForgetPasswordFragment() {
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
    }

    @OnClick(R.id.ib_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "back::ImageView has been clicked");
        replaceFragmentWithAnimation(LoginFragment.newInstance(), TAG_FRAG_LOGIN);
    }

    @OnClick(R.id.btn_send_email)
    public void onBtnSendEmailClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "onBtnSendEmailClicked() has been instantiated");

        if (!isNetworkAvailable()) {
            Log.d(TAG_FRAG_FORGET_PASS, "No Network Connection");
            showNoNetworkDialog();
            return;
        }

        final String email = edtForgetEmail.getText().toString();
        if (checkEmailValidation(email) && isNetworkAvailable()) {
            Log.d(TAG_FRAG_FORGET_PASS, "Valid Email address");

            tvErrorMessage.setVisibility(View.GONE);
            startActivity(new Intent(getContext(), LoadingActivity.class));

            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            Call<Employee> call = service.sendEmailToResetPassword(email);

            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_FORGET_PASS, "Response code -> " + response.code() + " " + response.message() + " ");
                        currentEmployee = response.body();
                        if (currentEmployee.getError() == null) {
                            Bundle extraEmail = new Bundle();
                            extraEmail.putString("extra_email", email);
                            VerifyIdentityFragment fragment = new VerifyIdentityFragment();
                            fragment.setArguments(extraEmail);
                            replaceFragmentWithAnimation(fragment, TAG_FRAG_VERIFY_IDENTITY);
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                        } else {
                            getResponseErrorMessage(getActivity(), response);
                        }
                    } else {
                        getResponseErrorMessage(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    Log.e(TAG_FRAG_FORGET_PASS, "onFailure(): " + t.toString());
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
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
    public boolean isNetworkAvailable() {
        Log.d(TAG_FRAG_FORGET_PASS, "isNetworkAvailable() has been instantiated");
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showNoNetworkDialog() {
        Log.d(TAG_FRAG_FORGET_PASS, "showNoNetworkDialog() has been instantiated");

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

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_FORGET_PASS, "replaceFragmentWithAnimation() has been instantiated");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.commitAllowingStateLoss();
    }

//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (Constants.sDisableFragmentAnimations) {
//            Animation a = new Animation() {
//            };
//            a.setDuration(0);
//            return a;
//        }
//        return super.onCreateAnimation(transit, enter, nextAnim);
//    }

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
                Employee errorModel = gson.fromJson(response.errorBody().string(), Employee.class);
                if (errorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_FORGET_PASS, "getLoginResponseErrorMessage() errorModel.Message = " + errorModel.getMessage());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(errorModel.getMessage());
                } else {
                    Log.i(TAG_FRAG_FORGET_PASS, "getLoginResponseErrorMessage() errorModel.Message = SOMETHING_WENT_WRONG");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.code() == 500) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

}