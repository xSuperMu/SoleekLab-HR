package com.example.moham.soleeklab.Fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moham.soleeklab.Interfaces.VacationFragmentInterface;
import com.example.moham.soleeklab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VACATION;

public class VacationFragment extends Fragment implements VacationFragmentInterface {

    @BindView(R.id.tv_action_bar_vacation)
    TextView tvActionBarVacation;
    @BindView(R.id.iv_vacation_icon)
    ImageView ivVacationIcon;
    @BindView(R.id.tv_vacation_text)
    TextView tvVacationText;
    @BindView(R.id.btn_new_vacation)
    Button btnNewVacation;
    @BindView(R.id.cl_no_vacation)
    ConstraintLayout clNoVacation;
    Unbinder unbinder;

    public VacationFragment() {
    }

    public static VacationFragment newInstance() {
        return new VacationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_VACATION, "onCreateView() has been instantiated");
        View view = inflater.inflate(R.layout.fragment_vacation, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_VACATION, "onDestroyView() has been instantiated");
        unbinder.unbind();
    }

    @OnClick(R.id.btn_new_vacation)
    @Override
    public void handleNewVacation() {
        Log.d(TAG_FRAG_VACATION, "handleNewVacation() has been instantiated");
        Log.d(TAG_FRAG_VACATION, "NewVacation::Button has been Clicked");
//        switchFragment(NewVacationFragment.newInstance(), TAG_FRAG_NEW_VACATION);
    }

    @Override
    public void switchFragment(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_VACATION, "switchFragment() has been instantiated");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


}
