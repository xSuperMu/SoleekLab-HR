package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Model.Requests.VacationRequests;
import com.example.moham.soleeklab.Model.Responses.ApplyForVacationResponse;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_VACATION_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NEW_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

public class NewVacationFragment extends Fragment {

    @BindView(R.id.tv_action_bar_new_vacation)
    TextView tvActionBarNewVacation;
    @BindView(R.id.et_starting_date)
    EditText edtStartingDate;
    @BindView(R.id.til_starting_date)
    TextInputLayout tilStartingDate;
    Unbinder unbinder;
    @BindView(R.id.spinner_vacation)
    AppCompatSpinner spinnerVacation;
    @BindView(R.id.tv_for_text)
    TextView tvForText;
    @BindView(R.id.tv_vacation_type)
    TextView tvVacationType;
    @BindView(R.id.spinner_vacation_type)
    AppCompatSpinner spinnerVacationType;
    @BindView(R.id.et_vacation_reason)
    EditText etVacationReason;
    @BindView(R.id.til_vacation_reason)
    TextInputLayout tilVacationReason;
    VacationReceiver mVacationReceiver;
    @BindView(R.id.tv_new_vacation)
    TextView tvNewVacation;
    @BindView(R.id.tv_cancel_new_vacation)
    TextView tvCancelNewVacation;

    DatePickerDialog mDatePickerDialog;
    Calendar mCalendar;
    HeaderInjector headerInjector;
    int mVacationNumOfDays;
    String mVacationType;
    String vacationDate = null;
    String vacationReason = null;
    HomeActivity mHomeActivity;
    private Call<ApplyForVacationResponse> mVacationRequestCall;

    public NewVacationFragment() {
    }

    public static NewVacationFragment newInstance() {
        return new NewVacationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_NEW_VACATION, "onCreateView() has been instantiated");

        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_VACATION_POS - 1).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_new_vacation, container, false);

        unbinder = ButterKnife.bind(this, view);
        instantiateViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_NEW_VACATION, "onAttach() has been instantiated");

        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_NEW_VACATION, "onDestroyView() has been instantiated");
        unbinder.unbind();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mVacationReceiver);
    }

    @OnClick({R.id.til_starting_date, R.id.et_starting_date})
    public void handleStartDate() {

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String inputFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(inputFormat, Locale.US);
                edtStartingDate.setText(sdf.format(myCalendar.getTime()));
                Date edTextDate = null;
                try {
                    edTextDate = sdf.parse(edtStartingDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
                Log.d(TAG_FRAG_NEW_VACATION, "vacationDate: " + outputFormatter.format(edTextDate));
                vacationDate = outputFormatter.format(edTextDate);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void instantiateViews() {
        Log.d(TAG_FRAG_NEW_VACATION, "instantiateViews() has been instantiated");

        headerInjector = new HeaderInjectorImplementation(getActivity());


        edtStartingDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (v != null && tilStartingDate != null) {
                        if (((EditText) v).getText().toString().length() == 0)
                            tilStartingDate.setError(null);
                    }
                }
            }
        });

        etVacationReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (v != null && tilVacationReason != null) {
                        if (((EditText) v).getText().toString().length() == 0)
                            tilVacationReason.setError(null);
                    }
                }
            }
        });

        //        TextWatcher mTextWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String reason = etVacationReason.getText().toString();
//                if (!TextUtils.isEmpty(reason)) {
//
//                } else {
//
//                }
//            }
//        };
        // Instantiating spinners
        final ArrayAdapter<CharSequence> spinnerVacationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.vacation_days, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> spinnerVacationTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.vacation_type_array, android.R.layout.simple_spinner_item);
        spinnerVacationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVacationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVacation.setAdapter(spinnerVacationAdapter);
        spinnerVacationType.setAdapter(spinnerVacationTypeAdapter);

        spinnerVacationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                Log.d(TAG_FRAG_NEW_VACATION, "spinner vacation type item selected: " + spinnerVacationTypeAdapter.getItem(position));
                mVacationType = (String) spinnerVacationTypeAdapter.getItem(position);
//                tvVacationType.setTextColor(getResources().getColor(R.color.colorBlue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mVacationType = "Travelling";
            }
        });

        spinnerVacation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                Log.d(TAG_FRAG_NEW_VACATION, "spinner vacation period item selected: " + spinnerVacationAdapter.getItem(position));
                mVacationNumOfDays = getVacationNumberOfDaysFromString(position);
//                tvForText.setTextColor(getResources().getColor(R.color.colorBlue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
                mVacationNumOfDays = 1;
            }
        });
    }

//    @OnClick({R.id.tv_new_vacation, R.id.tv_cancel_new_vacation})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_new_vacation:
//                Log.d(TAG_FRAG_NEW_VACATION, "RequestVacation::Button has been clicked");
//                requestNewVacation();
//                break;
//            case R.id.tv_cancel_new_vacation:
//                Log.d(TAG_FRAG_NEW_VACATION, "CancelVacation::Button has been clicked");
//                switchFragment(VacationFragment.newInstance(), TAG_FRAG_VACATION);
//                break;
//        }
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_FRAG_NEW_VACATION, "onCreate() has been instantiated");

        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CANCEL_VACATION);
        mVacationReceiver = new VacationReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mVacationReceiver, filter);
    }

    void handleVacationOnSameDay() {
        Log.d(TAG_FRAG_NEW_VACATION, "handleVacationOnSameDay() has been instantiated");
        Toast.makeText(getActivity(), "You've already requested a vacation on the same day", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
    }

    void handleVacationRequested() {
        Log.d(TAG_FRAG_NEW_VACATION, "handleVacationRequested() has been instantiated");
        Toast.makeText(getActivity(), "Vacation has been sent to the HR", Toast.LENGTH_SHORT).show();
        switchFragment(VacationFragment.newInstance(), TAG_FRAG_VACATION);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
    }

    void requestNewVacation() {
        Log.d(TAG_FRAG_NEW_VACATION, "requestNewVacation() has been instantiated");


//        show loading screen

        vacationReason = etVacationReason.getText().toString();
        Log.d(TAG_FRAG_NEW_VACATION, "vacationReason: " + vacationReason);
        // Check data validation
        if (!checkCredentialsValidation(vacationReason))
            return;
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_NEW_VACATION, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }

        // Do network calls
        ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);

        VacationRequests requests = new VacationRequests(vacationDate, mVacationNumOfDays, vacationReason, mVacationType);

        mVacationRequestCall = service.requestVacation(headerInjector.getHeaders(), requests);

        mVacationRequestCall.enqueue(new Callback<ApplyForVacationResponse>() {
            @Override
            public void onResponse(Call<ApplyForVacationResponse> call, Response<ApplyForVacationResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG_FRAG_NEW_VACATION, "Response code -> " + response.code() + " " + response.message() + " ");
                    handleVacationRequested();
                } else {
                    handleVacationOnSameDay();
                }
            }

            @Override
            public void onFailure(Call<ApplyForVacationResponse> call, Throwable t) {
                Log.e(TAG_FRAG_NEW_VACATION, "onFailure(): " + t.toString());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                if (call.isCanceled())
                    Toast.makeText(getActivity(), "Canceled!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void switchFragment(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_NEW_VACATION, "switchFragment() has been instantiated");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, fragment, tag);
        transaction.commit();
    }

    public boolean checkCredentialsValidation(String reason) {
        Log.d(TAG_FRAG_NEW_VACATION, "checkCredentialsValidation() has been instantiated");

        if (!TextUtils.isEmpty(vacationDate))
            if (!TextUtils.isEmpty(reason)) {
                tilVacationReason.setError(null);
                tilStartingDate.setError(null);
                return true;
            } else
                tilVacationReason.setError("Enter a reason");
        else
            tilStartingDate.setError("Select a date");
        return false;
    }


    @OnClick(R.id.tv_new_vacation)
    public void onTvNewVacationClicked() {
        Log.d(TAG_FRAG_NEW_VACATION, "RequestVacation::Button has been clicked");
        requestNewVacation();
    }

    @OnClick(R.id.tv_cancel_new_vacation)
    public void onTvCancelNewVacationClicked() {
        Log.d(TAG_FRAG_NEW_VACATION, "CancelVacation::Button has been clicked");
        switchFragment(VacationFragment.newInstance(), TAG_FRAG_VACATION);
    }

    class VacationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_FRAG_NEW_VACATION, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CANCEL_VACATION)) {
                Log.d(TAG_FRAG_NEW_VACATION, "cancelling Vacation request");
//                getVacationRequestCall.cancel();
            }
        }
    }

    public int getVacationNumberOfDaysFromString(int spinnerVacationPosition) {
        Log.d(TAG_FRAG_NEW_VACATION, "getVacationNumberOfDaysFromString() has been instantiated");
        switch (spinnerVacationPosition) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 9;
            default:
                return 10;
        }
    }
}