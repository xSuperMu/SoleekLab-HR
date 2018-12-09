package com.example.moham.soleeklab.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
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
import android.widget.EditText;
import android.widget.ImageView;

import com.example.moham.soleeklab.Interfaces.ResettingPassInterface;
import com.example.moham.soleeklab.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.moham.soleeklab.Utils.Constants.INT_NEW_PASS;
import static com.example.moham.soleeklab.Utils.Constants.INT_RETYPED_PASS;
import static com.example.moham.soleeklab.Utils.Constants.LOGIN_PASS_MIN_LENGTH;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_RESET_PASS;

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

    private TextWatcher mPassTextWatcher;
    private TextWatcher mRetypedPassTextWatcher;
    private TextWatcher mEnableBtnTextWatcher;

    public ResettingPasswordFragment() {
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
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.ib_back)
    public void handleBackClicked() {
        Log.d(TAG_FRAG_RESET_PASS, "back::ImageView has been clicked");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager != null)
            fragmentManager.popBackStack();
        else
            Log.w(TAG_FRAG_RESET_PASS, "FragmentManager -> null");
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
        tilRetypeNewPasswordLayout.setError(getString(R.string.error_short_password));
        return false;
    }

    @OnClick(R.id.reset)
    @Override
    public void resetPassword() {
        Log.d(TAG_FRAG_RESET_PASS, "btnReset::Button has been clicked");

        if (!isNetworkAvailable()) {
            Log.d(TAG_FRAG_RESET_PASS, "No network, Showing the dialog");
            showNoNetworkDialog();
            return;
        }

        String pass = edtNewPassword.getText().toString();
        String retypedPass = edtRetypeNewPassword.getText().toString();

        if (checkPasswordValidation(pass, INT_NEW_PASS) && checkPasswordValidation(retypedPass, INT_RETYPED_PASS) && checkPasswordMatch(pass, retypedPass)) {
            Log.d(TAG_FRAG_RESET_PASS, "Resetting user password");

            // Do reset
            final AlertDialog.Builder showLoadingDialog = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.loading, null);
            showLoadingDialog.setView(view);

            try {
                GifImageView gifImageView = view.findViewById(R.id.gif_loading);
                GifDrawable gifFromAssets = new GifDrawable(getActivity().getAssets(), "logoloading.gif");
                gifImageView.setImageDrawable(gifFromAssets);
            } catch (IOException e) {
                e.printStackTrace();
            }


            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

            final AlertDialog dialog = showLoadingDialog.create();
            dialog.show();
            dialog.getWindow().setAttributes(layoutParams);
            dialog.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_RESET_PASS, "instantiateViews() has been instantiated");

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        btnReset.setText(getString(R.string.reset));
        btnReset.setBackgroundResource(R.drawable.button_gray);
        btnReset.setEnabled(false);

        mPassTextWatcher = new TextWatcher() {
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

        mRetypedPassTextWatcher = new TextWatcher() {
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

        mEnableBtnTextWatcher = new TextWatcher() {
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
    public void showNoNetworkDialog() {
        Log.d(TAG_FRAG_RESET_PASS, "showNoNetworkDialog() has been instantiated");
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
    public boolean isNetworkAvailable() {
        Log.d(TAG_FRAG_RESET_PASS, "isNetworkAvailable() has been instantiated");
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_RESET_PASS, "replaceFragmentWithAnimation() has been instantiated");
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
