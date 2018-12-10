package com.example.moham.soleeklab.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Interfaces.AuthLoginInterface;
import com.example.moham.soleeklab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.LOGIN_PASS_MIN_LENGTH;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;

public class LoginFragment extends Fragment implements AuthLoginInterface {

    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.error_message)
    TextView tvErrorMessage;
    @BindView(R.id.login_email)
    EditText edtLoginEmail;
    @BindView(R.id.input_email_layout)
    TextInputLayout tilInputEmailLayout;
    @BindView(R.id.login_password)
    EditText edtLoginPassword;
    @BindView(R.id.input_password_layout)
    TextInputLayout tilInputPasswordLayout;
    Unbinder unbinder;
    @BindView(R.id.login_btn)
    Button btnLoginBtn;
    @BindView(R.id.sv_login_fragment)
    ScrollView svLoginFragment;

    private TextWatcher mEmailTextWatcher;
    private TextWatcher mPasswordTextWatcher;
    private TextWatcher mTextWatcher;
    private ProgressDialog mLoadingDialog;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        Log.d(TAG_FRAG_LOGIN, "newInstance() has been instantiated");
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_LOGIN, "onCreateView() has been instantiated");

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG_FRAG_LOGIN, "onResume() has been instantiated");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_LOGIN, "onDestroyView() has been instantiated");

        unbinder.unbind();

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.tv_forget_password)
    public void onViewClicked() {
        Log.d(TAG_FRAG_LOGIN, "ForgetPassword::TextView has been clicked");
        replaceFragmentWithAnimation(new ForgetPasswordFragment(), TAG_FRAG_FORGET_PASS);
    }

    @Override
    public void replaceFragmentWithAnimation(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_LOGIN, "replaceFragmentWithAnimation() has been instantiated");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
//                R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public boolean checkEmailValidation(String email) {
        Log.d(TAG_FRAG_LOGIN, "checkEmailValidation() has been instantiated");
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilInputEmailLayout.setError(null);
            return true;
        } else {
            tilInputEmailLayout.setError(getString(R.string.error_invalid_email));
            return false;
        }
    }

    @Override
    public boolean checkPasswordValidation(String password) {
        Log.d(TAG_FRAG_LOGIN, "checkPasswordValidation() has been instantiated");

        int passLength = edtLoginPassword.getText().toString().length();
        if (passLength >= LOGIN_PASS_MIN_LENGTH) {
            tilInputPasswordLayout.setError(null);
            return true;
        } else {
            tilInputPasswordLayout.setError(getString(R.string.error_short_password));
            return false;
        }
    }

    @Override
    @OnClick(R.id.login_btn)
    public void tryToLogin() {
        Log.d(TAG_FRAG_LOGIN, "Login::Button has been clicked");

        String email = edtLoginEmail.getText().toString();
        String password = edtLoginPassword.getText().toString();

        if (!isNetworkAvailable()) {
            Log.d(TAG_FRAG_LOGIN, "No network, Showing the dialog");

            showNoNetworkDialog();

            return;
        }

        if (checkEmailValidation(email) && checkPasswordValidation(password) && isNetworkAvailable()) {
            Log.d(TAG_FRAG_LOGIN, "Logging user in");
//            mLoadingDialog = ProgressDialog.show(getActivity(), "", "Logging you in ...", false, true);
//            mLoadingDialog.setCanceledOnTouchOutside(false);

            // mLoadingDialog.show();

//            final AlertDialog.Builder showLoadingDialog = new AlertDialog.Builder(getContext());
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            View view = inflater.inflate(R.layout.loading, null);
//            showLoadingDialog.setView(view);

//
//
//
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//            final AlertDialog dialog = showLoadingDialog.create();
//            dialog.show();
//            dialog.getWindow().setAttributes(layoutParams);
//            dialog.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            startActivity(new Intent(getContext(), LoadingActivity.class));
        }
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_LOGIN, "instantiateViews() has been instantiated");

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        btnLoginBtn.setText(getString(R.string.login));
        btnLoginBtn.setBackgroundResource(R.drawable.button_gray);
        btnLoginBtn.setEnabled(false);

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = edtLoginEmail.getText().toString();
                String password = edtLoginPassword.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    btnLoginBtn.setEnabled(true);
                    btnLoginBtn.setBackgroundResource(R.drawable.button_green);
                } else {
                    btnLoginBtn.setEnabled(false);
                    btnLoginBtn.setBackgroundResource(R.drawable.button_gray);
                }
            }
        };

        mEmailTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = edtLoginEmail.getText().toString();

                if (!TextUtils.isEmpty(email)) {
                    DrawableCompat.setTint(edtLoginEmail.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorBlue));

                } else {
                    DrawableCompat.setTint(edtLoginEmail.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorGray));
                }
            }
        };

        mPasswordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = edtLoginPassword.getText().toString();
                if (!TextUtils.isEmpty(password)) {
                    DrawableCompat.setTint(edtLoginPassword.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorBlue));
                } else {
                    DrawableCompat.setTint(edtLoginPassword.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorGray));

                }
            }
        };

        edtLoginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (v != null && tilInputEmailLayout != null) {
//                        DrawableCompat.setTint(edtLoginEmail.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorGray));
                        if (((EditText) v).getText().toString().length() == 0)
                            tilInputEmailLayout.setError(null);
                    }
                }
//                else {
//                    if (v != null && tilInputEmailLayout != null)
//                        DrawableCompat.setTint(edtLoginEmail.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorBlue));
//                }
            }
        });

        edtLoginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (v != null && tilInputPasswordLayout != null) {
//                        DrawableCompat.setTint(edtLoginEmail.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorGray));
                        if (((EditText) v).getText().toString().length() == 0)
                            tilInputPasswordLayout.setError(null);
                    }
                }
//                else {
//                    if (v != null && tilInputPasswordLayout != null)
//                        DrawableCompat.setTint(edtLoginPassword.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorBlue));
//                }
            }
        });

        edtLoginEmail.addTextChangedListener(mEmailTextWatcher);
        edtLoginPassword.addTextChangedListener(mPasswordTextWatcher);
        edtLoginEmail.addTextChangedListener(mTextWatcher);
        edtLoginPassword.addTextChangedListener(mTextWatcher);
    }

    @Override
    public boolean isNetworkAvailable() {
        Log.d(TAG_FRAG_LOGIN, "isNetworkAvailable() has been instantiated");
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showNoNetworkDialog() {
        Log.d(TAG_FRAG_LOGIN, "showNoNetworkDialog() has been instantiated");

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

    @OnClick({R.id.input_email_layout, R.id.input_password_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.input_email_layout:
                break;
            case R.id.input_password_layout:
                break;
        }
    }

}
