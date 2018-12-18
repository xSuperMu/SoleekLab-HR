package com.example.moham.soleeklab.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moham.soleeklab.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NEW_VACATION;

public class NewVacationFragment extends Fragment {

    DatePickerDialog mDatePickerDialog;
    Calendar mCalendar;
    @BindView(R.id.tv_action_bar_new_vacation)
    TextView tvActionBarNewVacation;
    @BindView(R.id.et_starting_date)
    EditText edtStartingDate;
    @BindView(R.id.til_starting_date)
    TextInputLayout tilStartingDate;
    Unbinder unbinder;
    @BindView(R.id.spinner_vacation)
    AppCompatSpinner spinnerVacation;
    String mVacationNumOfDays;
    String mVacationType;
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

    public NewVacationFragment() {
    }

    public static NewVacationFragment newInstance() {
        return new NewVacationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_NEW_VACATION, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_new_vacation, container, false);

        unbinder = ButterKnife.bind(this, view);
        instantiateViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_NEW_VACATION, "onDestroyView() has been instantiated");

        unbinder.unbind();
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
            }

        };

        new DatePickerDialog(getActivity(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void instantiateViews() {
        Log.d(TAG_FRAG_NEW_VACATION, "instantiateViews() has been instantiated");

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

}
