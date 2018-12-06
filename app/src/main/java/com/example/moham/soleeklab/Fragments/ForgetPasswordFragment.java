package com.example.moham.soleeklab.Fragments;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 */
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
    }

    @OnClick(R.id.ib_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "back::ImageView has been clicked");
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.btn_send_email)
    public void onBtnSendEmailClicked() {
        Log.d(TAG_FRAG_FORGET_PASS, "onBtnSendEmailClicked() has been instantiated");

        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), "No Network Connection", Toast.LENGTH_SHORT).show();
            return;
        }


        String email = edtForgetEmail.getText().toString();
        if (checkEmailValidation(email) && isNetworkAvailable()) {
            Toast.makeText(getActivity(), "Sending . . . ", Toast.LENGTH_SHORT).show();

//            mLoadingDialog = ProgressDialog.show(getActivity(), "", "Please wait a moment ...", false, true);
//            mLoadingDialog.setCanceledOnTouchOutside(false);

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


        btnSendEmail.setText(getString(R.string.send_email));
        btnSendEmail.setBackgroundResource(R.drawable.button_gray);
        btnSendEmail.setEnabled(false);

        mEmailTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            @Override
            public void afterTextChanged(Editable s) {

            }

        };
        edtForgetEmail.addTextChangedListener(mEmailTextWatcher);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}