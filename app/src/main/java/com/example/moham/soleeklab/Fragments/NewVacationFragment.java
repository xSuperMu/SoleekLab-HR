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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_VACATION_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NEW_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_VACATION;

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
    String mVacationNumOfDays;
    String mVacationType;
    String vacationDate;
    HomeActivity mHomeActivity;

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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mVacationReceiver);
    }

    @OnClick({R.id.til_starting_date, R.id.et_starting_date})
    public void handleStartDate() {

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edtStartingDate.setText(sdf.format(myCalendar.getTime()));

                String serverFormat = "yyyy-MM-dd";
                String edTextDate = edtStartingDate.getText().toString();
                Log.d(TAG_FRAG_NEW_VACATION, "Dat::Server Format ----> " + edTextDate);
                SimpleDateFormat serverSdf = new SimpleDateFormat(serverFormat, Locale.US);
                vacationDate = serverSdf.format(edTextDate);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private void instantiateViews() {
        Log.d(TAG_FRAG_NEW_VACATION, "instantiateViews() has been instantiated");

        headerInjector = new HeaderInjectorImplementation(getActivity());

        // Instantiating spinners
        final ArrayAdapter<CharSequence> spinnerVacationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.vacation_days, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> spinnerVacationTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.vacation_type_array, android.R.layout.simple_spinner_item);
        spinnerVacationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVacationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVacation.setAdapter(spinnerVacationAdapter);
        spinnerVacationType.setAdapter(spinnerVacationTypeAdapter);

        spinnerVacationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG_FRAG_NEW_VACATION, "spinner vacation type item selected: " + spinnerVacationTypeAdapter.getItem(position));
                mVacationType = (String) spinnerVacationTypeAdapter.getItem(position);
//                tvVacationType.setTextColor(getResources().getColor(R.color.colorBlue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mVacationType = "Travelling";
            }
        });

        spinnerVacation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG_FRAG_NEW_VACATION, "spinner vacation period item selected: " + spinnerVacationAdapter.getItem(position));
                mVacationNumOfDays = (String) spinnerVacationAdapter.getItem(position);
//                tvForText.setTextColor(getResources().getColor(R.color.colorBlue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
                mVacationNumOfDays = "1 Day";
            }
        });
    }

    @OnClick({R.id.tv_new_vacation, R.id.tv_cancel_new_vacation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_new_vacation:
                Log.d(TAG_FRAG_NEW_VACATION, "RequestVacation::Button has been clicked");
                requestNewVacation();
                break;
            case R.id.tv_cancel_new_vacation:
                Log.d(TAG_FRAG_NEW_VACATION, "CancelVacation::Button has been clicked");
                switchFragment(VacationFragment.newInstance(), TAG_FRAG_VACATION);
                break;
        }
    }

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
    }

    void handleVacationRequested() {
        Log.d(TAG_FRAG_NEW_VACATION, "handleVacationRequested() has been instantiated");
        Toast.makeText(getActivity(), "Vacation has been sent to the HR", Toast.LENGTH_SHORT).show();
    }

    void requestNewVacation() {
        Log.d(TAG_FRAG_NEW_VACATION, "requestNewVacation() has been instantiated");

        // Check data validation

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_NEW_VACATION, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            return;
        }


        // Do network calls
    }

    public void switchFragment(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_NEW_VACATION, "switchFragment() has been instantiated");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, fragment, tag);
        transaction.commit();
    }

    public boolean checkReasonValidation(String reason) {
        Log.d(TAG_FRAG_NEW_VACATION, "checkReasonValidation() has been instantiated");
        if (!TextUtils.isEmpty(reason))
            return true;
        if (TextUtils.isEmpty(reason)) {
            tilVacationReason.setError("Enter a reason");
        }
        return false;
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
}