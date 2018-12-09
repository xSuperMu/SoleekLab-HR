package com.example.moham.soleeklab.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.moham.soleeklab.Interfaces.AuthForgetPasswordInterface;
import com.example.moham.soleeklab.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;

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

    private ProgressDialog mLoadingDialog;
    private TextWatcher mEmailTextWatcher;

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
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.ib_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "back::ImageView has been clicked");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager != null)
            fragmentManager.popBackStack();
        else
            Log.w(TAG_FRAG_FORGET_PASS, "FragmentManager -> null");

    }

    @OnClick(R.id.btn_send_email)
    public void onBtnSendEmailClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "onBtnSendEmailClicked() has been instantiated");

        if (!isNetworkAvailable()) {
            Log.d(TAG_FRAG_FORGET_PASS, "No Network Connection");
            showNoNetworkDialog();
            return;
        }

        String email = edtForgetEmail.getText().toString();
        if (checkEmailValidation(email) && isNetworkAvailable()) {
            Log.d(TAG_FRAG_FORGET_PASS, "Valid Email address");
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

            if (email.equals("1@1.1")) {

                dialog.dismiss();

                VerifyIdentityFragment fragment = new VerifyIdentityFragment();
                Bundle extraEmail = new Bundle();
                extraEmail.putString("extra_email", email);
                fragment.setArguments(extraEmail);
                replaceFragmentWithAnimation(fragment, TAG_FRAG_VERIFY_IDENTITY);

            }


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

//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        btnSendEmail.setText(getString(R.string.send_email));
        btnSendEmail.setBackgroundResource(R.drawable.button_gray);
        btnSendEmail.setEnabled(false);

        mEmailTextWatcher = new TextWatcher() {
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
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}