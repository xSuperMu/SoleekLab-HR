package com.example.moham.soleeklab.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moham.soleeklab.Model.Vacation;
import com.example.moham.soleeklab.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_VACATION_ADAPTER;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private Context mContext;
    private List<Vacation> mVacationRequestList;

    public VacationAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG_VACATION_ADAPTER, "onCreateViewHolder() has been instantiated");
        View vacationListItem = LayoutInflater.from(mContext).inflate(R.layout.vacation_list_item, viewGroup, false);
        return new VacationViewHolder(vacationListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder vacationViewHolder, int i) {
        Log.d(TAG_VACATION_ADAPTER, "onBindViewHolder() has been instantiated");

        Vacation vacation = mVacationRequestList.get(i);

        int state;
        String adminStartDate = null;
        String adminEndDate = null;
        String vacationType = vacation.getVacationType();
        vacationViewHolder.tvVacationName.setText(vacationType);

        try {
            state = vacation.getState();
            adminStartDate = vacation.getAdminStartDate();
            adminEndDate = vacation.getAdminEndDate();
        } catch (NullPointerException e) {
            state = -1;
            adminStartDate = null;
            adminEndDate = null;
        }

        if (state != -1 && state == 1) {
            vacationViewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.drawable.round_view_green));
            vacationViewHolder.tvState.setText(mContext.getString(R.string.accepted));
            vacationViewHolder.llRejectionReason.setVisibility(View.GONE);

            String vacationReason = vacation.getVacationReason();
            vacationViewHolder.tvVacationReason.setText(vacationReason);

            if (TextUtils.isEmpty(adminStartDate) || TextUtils.isEmpty(adminEndDate)) {
                vacationViewHolder.llUserVacationPeriod.setVisibility(View.GONE);
                String adminStartDateStr = null;
                String adminEndDateStr = null;
                try {
                    adminStartDateStr = formatTime(vacation.getStartDate());
                    adminEndDateStr = formatTime(vacation.getEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                vacationViewHolder.tvAdminStartDate.setText(adminStartDateStr);
                vacationViewHolder.tvAdminEndDate.setText(adminEndDateStr);
                vacationViewHolder.llAdminVacationPeriod.setVisibility(View.VISIBLE);
            } else {
                String userStartDate = vacation.getStartDate();
                String userEndDate = vacation.getEndDate();
                String userStartDateStr = null;
                String userEndDateStr = null;

                String adminStartDateStr = null;
                String adminEndDateStr = null;
                try {
                    adminStartDateStr = formatTime(adminStartDate);
                    adminEndDateStr = formatTime(adminEndDate);
                    userStartDateStr = formatTime(userStartDate);
                    userEndDateStr = formatTime(userEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                vacationViewHolder.tvAdminStartDate.setText(adminStartDateStr);
                vacationViewHolder.tvAdminEndDate.setText(adminEndDateStr);
                vacationViewHolder.tvVacationStartData.setText(userStartDateStr);
                vacationViewHolder.tvVacationEndDate.setText(userEndDate);
                vacationViewHolder.tvVacationStartData.setPaintFlags(vacationViewHolder.tvVacationStartData.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                vacationViewHolder.tvVacationEndDate.setPaintFlags(vacationViewHolder.tvVacationEndDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                vacationViewHolder.llUserVacationPeriod.setVisibility(View.VISIBLE);
                vacationViewHolder.llAdminVacationPeriod.setVisibility(View.VISIBLE);
            }


        } else if (state != -1 && state == 2) {
            vacationViewHolder.tvState.setText(mContext.getString(R.string.rejected));
            vacationViewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.drawable.round_view_red));

            String vacationReason = vacation.getVacationReason();
            vacationViewHolder.tvVacationReason.setText(vacationReason);

            String rejectionReason = vacation.getRejectionReason();
            vacationViewHolder.tvRefusingReason.setText(rejectionReason);

            if (TextUtils.isEmpty(adminStartDate) || TextUtils.isEmpty(adminEndDate)) {
                vacationViewHolder.llUserVacationPeriod.setVisibility(View.GONE);
                String adminStartDateStr = null;
                String adminEndDateStr = null;
                try {
                    adminStartDateStr = formatTime(vacation.getStartDate());
                    adminEndDateStr = formatTime(vacation.getEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                vacationViewHolder.tvAdminStartDate.setText(adminStartDateStr);
                vacationViewHolder.tvAdminEndDate.setText(adminEndDateStr);
                vacationViewHolder.llAdminVacationPeriod.setVisibility(View.VISIBLE);
            } else {
                String userStartDate = vacation.getStartDate();
                String userEndDate = vacation.getEndDate();
                String userStartDateStr = null;
                String userEndDateStr = null;

                String adminStartDateStr = null;
                String adminEndDateStr = null;
                try {
                    adminStartDateStr = formatTime(adminStartDate);
                    adminEndDateStr = formatTime(adminEndDate);
                    userStartDateStr = formatTime(userStartDate);
                    userEndDateStr = formatTime(userEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                vacationViewHolder.tvAdminStartDate.setText(adminStartDateStr);
                vacationViewHolder.tvAdminEndDate.setText(adminEndDateStr);
                vacationViewHolder.tvVacationStartData.setText(userStartDateStr);
                vacationViewHolder.tvVacationEndDate.setText(userEndDate);
                vacationViewHolder.tvVacationEndDate.setPaintFlags(vacationViewHolder.tvVacationEndDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                vacationViewHolder.tvVacationStartData.setPaintFlags(vacationViewHolder.tvVacationStartData.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                vacationViewHolder.llUserVacationPeriod.setVisibility(View.VISIBLE);
                vacationViewHolder.llAdminVacationPeriod.setVisibility(View.VISIBLE);
            }

        } else {
            // state = null,  state == "pending"
            vacationViewHolder.tvState.setText(mContext.getString(R.string.pending));
            vacationViewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.drawable.round_view_skin));
            vacationViewHolder.llRejectionReason.setVisibility(View.GONE);
            vacationViewHolder.llUserVacationPeriod.setVisibility(View.GONE);

            String vacationReason = vacation.getVacationReason();
            vacationViewHolder.tvVacationReason.setText(vacationReason);

            String startDate = vacation.getStartDate();
            String endDate = vacation.getEndDate();
            String startDateStr = null;
            String endDateStr = null;
            try {
                startDateStr = formatTime(startDate);
                endDateStr = formatTime(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            vacationViewHolder.tvAdminStartDate.setText(startDateStr);
            vacationViewHolder.tvAdminEndDate.setText(endDateStr);
            vacationViewHolder.llAdminVacationPeriod.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mVacationRequestList != null)
            return mVacationRequestList.size();
        return 0;
    }

    public String formatTime(String timeToFormat) throws ParseException {
        Log.d(TAG_VACATION_ADAPTER, "formatTime() has been instantiated");

        String pattern = "MM/dd/yyyy";
        DateFormat formatter = new SimpleDateFormat(pattern);
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = originalFormat.parse(timeToFormat);
        return formatter.format(date);
    }

    public void swapVacationList(List<Vacation> newVacationList) {
        Log.d(TAG_VACATION_ADAPTER, "swapVacationList() has been instantiated");
        mVacationRequestList = newVacationList;
        notifyDataSetChanged();
    }

    public void extendVacationList(List<Vacation> extendedVacationList) {
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_vacation_name)
        public TextView tvVacationName;
        @BindView(R.id.tv_vacation_start_data)
        public TextView tvVacationStartData;
        @BindView(R.id.tv_vacation_end_date)
        public TextView tvVacationEndDate;
        @BindView(R.id.ll_user_vacation_period)
        public LinearLayout llUserVacationPeriod;
        @BindView(R.id.tv_admin_start_date)
        public TextView tvAdminStartDate;
        @BindView(R.id.tv_admin_end_date)
        public TextView tvAdminEndDate;
        @BindView(R.id.ll_admin_vacation_period)
        public LinearLayout llAdminVacationPeriod;
        @BindView(R.id.tv_vacation_reason)
        public TextView tvVacationReason;
        @BindView(R.id.iv_chat_icon)
        public ImageView ivChatIcon;
        @BindView(R.id.tv_refusing_reason)
        public TextView tvRefusingReason;
        @BindView(R.id.ll_rejection_reason)
        public LinearLayout llRejectionReason;
        @BindView(R.id.tv_state_view)
        public TextView tvState;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
