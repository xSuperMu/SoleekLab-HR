package com.example.moham.soleeklab.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Activities.LoadingActivity;
import com.example.moham.soleeklab.Interfaces.AuthLoginInterface;
import com.example.moham.soleeklab.Model.CheckInResponse;
import com.example.moham.soleeklab.Model.Employee;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;
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
import static com.example.moham.soleeklab.Utils.Constants.LOGIN_PASS_MIN_LENGTH;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_FORGET_PASS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE;

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
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_more_info)
    TextView tvMoreInfo;
    private HeaderInjector headerInjector;
    private Employee currentEmployee;

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
        transaction.replace(R.id.fragment_holder, fragment);
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

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_LOGIN, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        if (checkEmailValidation(email) && checkPasswordValidation(password) && NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_LOGIN, "Logging user in");

            tvErrorMessage.setVisibility(View.GONE);
            startActivity(new Intent(getContext(), LoadingActivity.class));

            ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
            Call<Employee> call = service.loginEmployee(email, password);

            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {

                    if (response.isSuccessful()) {
                        Log.e(TAG_FRAG_LOGIN, "Response code -> " + response.code() + " " + response.message() + " ");
                        Log.e(TAG_FRAG_LOGIN, "User Logged in successfully");
                        currentEmployee = response.body().getEmployee();
                        Log.e(TAG_FRAG_LOGIN, "Saving Employee to shared preferences");
                        EmployeeSharedPreferences.SaveEmployeeToPreferences(getActivity(), currentEmployee);

                        Log.e(TAG_FRAG_LOGIN, "Getting employee check in status");
                        ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);

                        Call<CheckInResponse> callGetAttendance = service.getTodayAttendance(headerInjector.getHeaders());
                        callGetAttendance.enqueue(new Callback<CheckInResponse>() {
                            @Override
                            public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                                if (response.isSuccessful()) {
                                    Log.e(TAG_FRAG_LOGIN, "Response code -> " + response.code() + " " + response.message() + " ");

                                    EmployeeSharedPreferences.SaveCheckInResponseToPreferences(getActivity(), response.body());
                                    Log.e(TAG_FRAG_LOGIN, "User Checked In Before, Moving to HomeActivity");
                                    startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("response code", 200));
                                    getActivity().finish();
                                    getActivity().sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                                } else {
                                    handleCheckInResponseError(getActivity(), response);
                                }
                            }

                            @Override
                            public void onFailure(Call<CheckInResponse> call, Throwable t) {
                                Log.e(TAG_FRAG_LOGIN, "onFailure(): " + t.toString());
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        getLoginResponseErrorMessage(getActivity(), response);
                    }
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {
                    Log.e(TAG_FRAG_LOGIN, "onFailure(): " + t.toString());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_LOGIN, "instantiateViews() has been instantiated");

        setFontsToViews();

        headerInjector = new HeaderInjectorImplementation(getContext());

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        tvErrorMessage.setVisibility(View.GONE);
        btnLoginBtn.setText(getString(R.string.login));
        btnLoginBtn.setBackgroundResource(R.drawable.button_gray);
        btnLoginBtn.setEnabled(false);

        TextWatcher mTextWatcher = new TextWatcher() {
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

        TextWatcher mEmailTextWatcher = new TextWatcher() {
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
//                    edtLoginEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_ATOP);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorBlue));
                        edtLoginEmail.setBackgroundTintList(colorStateList);
                        ViewCompat.setBackgroundTintList(edtLoginEmail, colorStateList);
                    }
                    edtLoginEmail.getBackground().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_ATOP);
                } else {
                    DrawableCompat.setTint(edtLoginEmail.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorGray));
//                    edtLoginEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorGray));
                        edtLoginEmail.setBackgroundTintList(colorStateList);
                        ViewCompat.setBackgroundTintList(edtLoginEmail, colorStateList);
                        edtLoginEmail.getBackground().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);

                    }
                }
            }
        };

        TextWatcher mPasswordTextWatcher = new TextWatcher() {
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorBlue));
                        edtLoginPassword.setBackgroundTintList(colorStateList);
                        ViewCompat.setBackgroundTintList(edtLoginPassword, colorStateList);
                    }
                    edtLoginPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_ATOP);

//                    edtLoginPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_ATOP);
                } else {
                    DrawableCompat.setTint(edtLoginPassword.getBackground(), ContextCompat.getColor(getActivity(), R.color.colorGray));
//                    edtLoginPassword.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorGray));
                        edtLoginPassword.setBackgroundTintList(colorStateList);
                        ViewCompat.setBackgroundTintList(edtLoginPassword, colorStateList);
                    }
                    edtLoginPassword.getBackground().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_ATOP);

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
            }
        });

        edtLoginEmail.addTextChangedListener(mEmailTextWatcher);
        edtLoginPassword.addTextChangedListener(mPasswordTextWatcher);
        edtLoginEmail.addTextChangedListener(mTextWatcher);
        edtLoginPassword.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void getLoginResponseErrorMessage(Context context, Response response) {
        Log.d(TAG_FRAG_LOGIN, "getLoginResponseErrorMessage() has been instantiated");
        if (response.code() == 401 || response.code() == 400) {
            Log.d(TAG_FRAG_LOGIN, "Response code ------> " + response.code() + " " + response.message());
            try {
                Gson gson = new Gson();
                Employee errorModel = gson.fromJson(response.errorBody().string(), Employee.class);
                if (errorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_LOGIN, "getLoginResponseErrorMessage() errorModel.Message = " + errorModel.getMessage());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(errorModel.getMessage());
                } else {
                    Log.i(TAG_FRAG_LOGIN, "getLoginResponseErrorMessage() errorModel.Message = SOMETHING_WENT_WRONG");
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

    @Override
    public void handleCheckInResponseError(Context context, Response response) {
        Log.d(TAG_FRAG_LOGIN, "handleCheckInResponseError() has been instantiated");
        if (response.code() == 404) {
            Log.d(TAG_FRAG_LOGIN, "Response code ------> " + response.code() + " " + response.message());
            Log.d(TAG_FRAG_LOGIN, "User Doesn't Checked In Before, Moving to MainActivity --> CheckIn Fragment");
            try {
                Gson gson = new Gson();
                CheckInResponse checkInErrorModel = gson.fromJson(response.errorBody().string(), CheckInResponse.class);
                if (checkInErrorModel.getMessage() != null) {
                    Log.i(TAG_FRAG_LOGIN, "handleCheckInResponseError() errorModel.Message = " + checkInErrorModel.getMessage());

                    EmployeeSharedPreferences.SaveCheckInResponseToPreferences(getActivity(), checkInErrorModel);

                    startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("response code", 404));
                    getActivity().finish();

                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_LOGIN, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_LOGIN, "setFontsToViews() has been instantiated");
        tvLogin.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tvMoreInfo.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        edtLoginEmail.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tilInputEmailLayout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tilInputPasswordLayout.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        edtLoginPassword.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        tvForgetPassword.setTypeface(loadFont(getContext(), FONT_DOSIS_REGULAR));
        btnLoginBtn.setTypeface(loadFont(getContext(), FONT_DOSIS_SEMI_BOLD));
    }
}