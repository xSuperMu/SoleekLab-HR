package com.example.moham.soleeklab.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.moham.soleeklab.Interfaces.VerifyIdentityInterface;
import com.example.moham.soleeklab.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VERIFY_IDENTITY;
import static com.example.moham.soleeklab.Utils.Constants.TOTAL_NUMBER_OF_ITEMS;

public class VerifyIdentityFragment extends Fragment implements VerifyIdentityInterface {

    @BindView(R.id.pinView)
    PinView pinView;
    Unbinder unbinder;
    @BindView(R.id.verify_email_message)
    TextView verifyEmailMessage;
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
    @BindView(R.id.ib_back)
    ImageView ibBack;
    @BindView(R.id.error_message)
    TextView errorMessage;

    private String verificationCode = null;

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

        instantiateViews();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "onDestroyView() has been instantiated");

        unbinder.unbind();
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "instantiateViews() has been instantiated");

        btnVerifyIdentity.setEnabled(false);
        btnVerifyIdentity.setBackgroundResource(R.drawable.button_gray);
        btnVerifyIdentity.setText(getString(R.string.verify));

        pinView.setItemRadius(10);
        pinView.setLineWidth(2);
        pinView.setAnimationEnable(true);
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
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

//                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                if (length == TOTAL_NUMBER_OF_ITEMS) {
                    Log.d(TAG_FRAG_VERIFY_IDENTITY, "verified code: " + s.toString());
                    verificationCode = s.toString();
                    btnVerifyIdentity.setEnabled(true);
                    btnVerifyIdentity.setBackgroundResource(R.drawable.button_green);

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
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

        if (!TextUtils.isEmpty(verificationCode)) {

            Toast.makeText(getActivity(), "Verifying . . .", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.ib_back)
    public void onIbBackClicked() {
        Log.d(TAG_FRAG_VERIFY_IDENTITY, "back::ImageView has been clicked");

        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

    }
}
