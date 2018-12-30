package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Adapter.VacationAdapter;
import com.example.moham.soleeklab.Interfaces.VacationFragmentInterface;
import com.example.moham.soleeklab.Model.Responses.VacationResponse;
import com.example.moham.soleeklab.Model.Vacation;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_VACATION_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_ATTENDANCE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_NEW_VACATION;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_VACATION;

public class VacationFragment extends Fragment implements VacationFragmentInterface, SwipeRefreshLayout.OnRefreshListener {

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
    HomeActivity mHomeActivity;
    @BindView(R.id.iv_new_vacation)
    ImageView ivNewVacation;
    @BindView(R.id.rv_vacation)
    RecyclerView rvVacation;
    @BindView(R.id.srl_vacation_swipe)
    SwipeRefreshLayout srlVacationSwipe;
    @BindView(R.id.cl_vacation)
    ConstraintLayout clVacation;
    @BindView(R.id.tv_action_bar_vacation_count)
    TextView tvActionBarVacationCount;

    private VacationAdapter mVacationAdapter;
    private HeaderInjector headerInjector;
    private Call<VacationResponse> getVacationRequestCall;
    private List<Vacation> mVacationList;


    public VacationFragment() {
    }


    public static VacationFragment newInstance() {
        return new VacationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_VACATION, "onCreateView() has been instantiated");
        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_VACATION_POS - 1).setChecked(true);

        View view = inflater.inflate(R.layout.fragment_vacation, container, false);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_VACATION, "onDestroyView() has been instantiated");
        unbinder.unbind();
        srlVacationSwipe = new SwipeRefreshLayout(getActivity());
    }

    @OnClick({R.id.btn_new_vacation, R.id.iv_new_vacation})
    @Override
    public void handleNewVacation() {
        Log.d(TAG_FRAG_VACATION, "handleNewVacation() has been instantiated");
        Log.d(TAG_FRAG_VACATION, "NewVacation::Button has been Clicked");
        switchFragment(NewVacationFragment.newInstance(), TAG_FRAG_NEW_VACATION);
    }

    @Override
    public void switchFragment(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_VACATION, "switchFragment() has been instantiated");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_fragment_holder, fragment, tag);
        transaction.commit();
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_VACATION, "instantiateViews() has been instantiated");

        headerInjector = new HeaderInjectorImplementation(getActivity());
        // RecyclerView object dependencies
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvVacation.setLayoutManager(mLinearLayoutManager);
        rvVacation.setHasFixedSize(true);
        srlVacationSwipe.setOnRefreshListener(this);
        mVacationAdapter = new VacationAdapter(getActivity());
        rvVacation.setAdapter(mVacationAdapter);

        srlVacationSwipe.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        srlVacationSwipe.setRefreshing(true);


        loadVacationDate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_VACATION, "onAttach() has been instantiated");

        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }

    @Override
    public void showVacationDateView() {
        Log.d(TAG_FRAG_VACATION, "showVacationDateView() has been instantiated");
        clVacation.setVisibility(View.VISIBLE);
        clNoVacation.setVisibility(View.GONE);
    }

    @Override
    public void loadVacationDate() {
        Log.d(TAG_FRAG_VACATION, "loadVacationDate() has been instantiated");


        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_VACATION, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            srlVacationSwipe.setRefreshing(false);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new Date());
        Log.d(TAG_FRAG_ATTENDANCE, "date ----> " + date);
        // make network call
        final ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);

        getVacationRequestCall = service.getVacationHistory(headerInjector.getHeaders(), date);
        getVacationRequestCall.enqueue(new Callback<VacationResponse>() {
            @Override
            public void onResponse(Call<VacationResponse> call, Response<VacationResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG_FRAG_VACATION, "Response code -> " + response.code() + " " + response.message() + " ");

                    List<Vacation> list = response.body().getmVacation().getVacation();
                    Log.d(TAG_FRAG_VACATION, "VacationResponse List: " + response.body().getmVacation().getVacation().toString());

                    mVacationAdapter.swapVacationList(list);
                    int count = response.body().getmVacation().getCount();
                    Log.d(TAG_FRAG_VACATION, "Count: " + count);
                    String countStr = getResources().getString(R.string.task_progress, count, "21");
                    tvActionBarVacationCount.setText(countStr);
                    tvActionBarVacationCount.setVisibility(View.VISIBLE);
                    srlVacationSwipe.setRefreshing(false);
                    showVacationDateView();
                } else {
                    // Data object is empty
                    hideVacationDateView();
                }
            }

            @Override
            public void onFailure(Call<VacationResponse> call, Throwable t) {
                Log.e(TAG_FRAG_VACATION, "onFailure(): " + t.toString());
                srlVacationSwipe.setRefreshing(false);
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void hideVacationDateView() {
        Log.d(TAG_FRAG_VACATION, "hideVacationDateView() has been instantiated");
        clVacation.setVisibility(View.GONE);
        clNoVacation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        Log.d(TAG_FRAG_VACATION, "onRefresh() has been instantiated");
        // refresh the data
        srlVacationSwipe.setRefreshing(true);
        mVacationAdapter.swapVacationList(null);
        loadVacationDate();
    }
}