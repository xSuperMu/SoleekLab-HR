package com.example.moham.soleeklab.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moham.soleeklab.Activities.AuthActivity;
import com.example.moham.soleeklab.Adapter.AttendanceAdapter;
import com.example.moham.soleeklab.Interfaces.AttendanceFregInterface;
import com.example.moham.soleeklab.Model.AttendanceSheetResponse;
import com.example.moham.soleeklab.Network.ClientService;
import com.example.moham.soleeklab.Network.HeaderInjector;
import com.example.moham.soleeklab.Network.HeaderInjectorImplementation;
import com.example.moham.soleeklab.Network.NetworkUtils;
import com.example.moham.soleeklab.Network.RetrofitClientInstance;
import com.example.moham.soleeklab.R;
import com.example.moham.soleeklab.Utils.EmployeeSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_BOLD;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.TAG_ATTENDANCE_REC;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_ATTENDANCE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_ATTENDANCE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CANCEL_LOGIN;
import static com.example.moham.soleeklab.Utils.Constants.TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN;

public class AttendanceFragment extends Fragment implements AttendanceFregInterface, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_action_bar_attendance)
    TextView tvActionBarAttendance;
    @BindView(R.id.iv_no_attendance_icon)
    ImageView ivNoAttendanceIcon;
    @BindView(R.id.tv_no_attendance_text)
    TextView tvNoAttendanceText;
    @BindView(R.id.cl_no_attendance)
    ConstraintLayout clNoAttendance;
    @BindView(R.id.rv_attendance)
    RecyclerView rvAttendance;
    Unbinder unbinder;
    @BindView(R.id.srlAttendanceSwipe)
    SwipeRefreshLayout srlAttendanceSwipe;
    AttendanceAdapter mAttendanceAdapter;
    private List<AttendanceSheetResponse> mUserAttendances;
    private AttendanceReceiver mAttendanceReceiver;
    private HeaderInjector headerInjector;

    public AttendanceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_FRAG_ATTENDANCE, "onCreate() has been instantiated");

        IntentFilter filter = new IntentFilter(TAG_LOADING_RECEIVER_ACTION_CANCEL_ATTENDANCE);
        mAttendanceReceiver = new AttendanceReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mAttendanceReceiver, filter);
    }

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_FRAG_ATTENDANCE, "onCreateView() has been instantiated");

        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        unbinder = ButterKnife.bind(this, view);
        instantiateViews();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG_FRAG_ATTENDANCE, "onStart() has been instantiated");

        // show loading screen
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_ATTENDANCE, "instantiateViews() has been instantiated");

        setFontsToViews();

        headerInjector = new HeaderInjectorImplementation(getActivity());
        // RecyclerView object dependencies
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAttendance.setLayoutManager(mLinearLayoutManager);
        rvAttendance.setHasFixedSize(true);
        srlAttendanceSwipe.setOnRefreshListener(this);
        mAttendanceAdapter = new AttendanceAdapter(getActivity());
        rvAttendance.setAdapter(mAttendanceAdapter);

        srlAttendanceSwipe.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        srlAttendanceSwipe.setRefreshing(true);

        loadAttendanceData();

        Log.d(TAG_FRAG_ATTENDANCE, "instantiateViews() return");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_ATTENDANCE, "onDestroyView() has been instantiated");
        unbinder.unbind();

        // Destroy resources
        srlAttendanceSwipe = new SwipeRefreshLayout(getActivity());
    }

    @Override
    public void onRefresh() {
        Log.d(TAG_FRAG_ATTENDANCE, "onRefresh() has been instantiated");
        // refresh the data
        srlAttendanceSwipe.setRefreshing(true);
        mAttendanceAdapter.swapAttendanceDataList(null);
        loadAttendanceData();
    }

    @Override
    public void loadAttendanceData() {
        Log.d(TAG_FRAG_ATTENDANCE, "loadAttendanceData() has been instantiated");

        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            Log.d(TAG_FRAG_ATTENDANCE, "No Network Connection");
            NetworkUtils.showNoNetworkDialog(getActivity());
            srlAttendanceSwipe.setRefreshing(false);
            return;
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new Date());
        Log.d(TAG_FRAG_ATTENDANCE, "date ----> " + date);
        ClientService service = RetrofitClientInstance.getRetrofitInstance().create(ClientService.class);
        Call<AttendanceSheetResponse> loadAttendanceSheet = service.getUserAttendanceSheet(headerInjector.getHeaders(), date);
        loadAttendanceSheet.enqueue(new Callback<AttendanceSheetResponse>() {
            @Override
            public void onResponse(Call<AttendanceSheetResponse> call, Response<AttendanceSheetResponse> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG_FRAG_ATTENDANCE, "Response code -> " + response.code() + " " + response.message() + " ");

                    // TODO (1)  show no attendance history if size() == 0

                    List<AttendanceSheetResponse> upsideDown = response.body().getAttendanceSheetResponse();
                    Collections.reverse(upsideDown);
                    if (upsideDown.size() == 0) {
                        srlAttendanceSwipe.setVisibility(View.GONE);
                        clNoAttendance.setVisibility(View.VISIBLE);
                    } else {
                        Log.e(TAG_FRAG_ATTENDANCE, "swapping Attendance Data List");
                        mAttendanceAdapter.swapAttendanceDataList(upsideDown);
                        srlAttendanceSwipe.setRefreshing(false);
                        srlAttendanceSwipe.setVisibility(View.VISIBLE);
                        clNoAttendance.setVisibility(View.GONE);
                    }
                } else {
                    handleAttendanceSheetResponseError(getActivity(), response);
                }
            }

            @Override
            public void onFailure(Call<AttendanceSheetResponse> call, Throwable t) {
                Log.e(TAG_FRAG_ATTENDANCE, "onFailure(): " + t.toString());
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(TAG_LOADING_RECEIVER_ACTION_CLOSE_LOADING_SCREEN));
                if (call.isCanceled())
                    Toast.makeText(getActivity(), "Canceled!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_ATTENDANCE, "loadFont() has been instantiated");
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_ATTENDANCE, "setFontsToViews() has been instantiated");
        tvActionBarAttendance.setTypeface(loadFont(getActivity(), FONT_DOSIS_BOLD));
        tvNoAttendanceText.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
    }

    public void handleAttendanceSheetResponseError(Context context, Response response) {
        Log.d(TAG_FRAG_ATTENDANCE, "handleAttendanceSheetResponseError() has been instantiated");
        if (response.code() == 404) {
            Log.d(TAG_FRAG_LOGIN, "Response code ------> " + response.code() + " " + response.message());
            Log.d(TAG_FRAG_LOGIN, "Moving to LoginFragment ");
            EmployeeSharedPreferences.clearPreferences(getActivity());
            Intent authIntent = new Intent(getActivity(), AuthActivity.class);
            getActivity().startActivity(authIntent);
            getActivity().finish();
        }
    }

    class AttendanceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_ATTENDANCE_REC, "onReceive() has been instantiated");
            if (intent.getAction().equals(TAG_LOADING_RECEIVER_ACTION_CANCEL_LOGIN)) {
                Log.d(TAG_ATTENDANCE_REC, "cancelling attendance request");
//                loginRequestCall.cancel();
            }
        }
    }
}
