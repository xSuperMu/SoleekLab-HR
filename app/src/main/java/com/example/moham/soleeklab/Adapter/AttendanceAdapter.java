package com.example.moham.soleeklab.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moham.soleeklab.Model.Responses.AttendanceSheetResponse;
import com.example.moham.soleeklab.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_ATTENDANCE_ADAPTER;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_ATTENDANCE;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private Context mContext;
    private List<AttendanceSheetResponse> mAttendanceList = null;
    public AttendanceAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG_ATTENDANCE_ADAPTER, "onCreateViewHolder() has been instantiated");

        View attendanceListItem = LayoutInflater.from(mContext).inflate(R.layout.attendance_list_item, viewGroup, false);
        return new AttendanceViewHolder(attendanceListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, int i) {
        Log.d(TAG_ATTENDANCE_ADAPTER, "onBindViewHolder() has been instantiated");

        AttendanceSheetResponse userAttendance = mAttendanceList.get(i);

        int empState = userAttendance.getState();


        String dayDate = userAttendance.getDay();
        attendanceViewHolder.tvDate.setText(dayDate);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = originalFormat.parse(dayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String getDayName = sdf.format(date);
        if (i == 0)
            attendanceViewHolder.tvWeekDay.setText("Today, ");
        else if (i == 1)
            attendanceViewHolder.tvWeekDay.setText("Yesterday, ");
        else
            attendanceViewHolder.tvWeekDay.setText(getDayName + ", ");

        if (empState == 1) {
            attendanceViewHolder.tvStatus.setVisibility(View.GONE);
            attendanceViewHolder.llWorkTime.setVisibility(View.VISIBLE);
            attendanceViewHolder.tvTotalWorkItem.setVisibility(View.VISIBLE);

            String checkInTime = userAttendance.getCheckIn();
            String checkInTimeStr = null;
            try {
                checkInTimeStr = formatTime(checkInTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(checkInTimeStr)) {
                attendanceViewHolder.tvCheckInTime.setText(checkInTimeStr);
            }

            String checkoutTime = null;
            String checkoutTimeStr = null;

            try {
                checkoutTime = userAttendance.getCheckOut();
                checkoutTimeStr = formatTime(checkoutTime);
            } catch (NullPointerException e) {
                checkoutTimeStr = "NA";
                checkoutTime = null;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (checkoutTimeStr.equals("NA")) {
                attendanceViewHolder.tvCheckOutTime.setText("NA");
                attendanceViewHolder.tvTotalWorkItem.setText("NA");
            } else {

                if (!TextUtils.isEmpty(checkoutTimeStr)) {
                    attendanceViewHolder.tvCheckOutTime.setText(checkoutTimeStr);
                    try {
                        attendanceViewHolder.tvTotalWorkItem.setText(subtractDates(checkoutTime, checkInTime));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            attendanceViewHolder.tvStatus.setVisibility(View.VISIBLE);
            attendanceViewHolder.llWorkTime.setVisibility(View.GONE);

            if (empState == 2) {
                attendanceViewHolder.tvStatus.setText("Absence");
                attendanceViewHolder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.round_view_red));
            } else if (empState == 3) {
                attendanceViewHolder.tvStatus.setText("Vacation");
                attendanceViewHolder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.round_view_green));
            } else {
                attendanceViewHolder.tvStatus.setText("Empty");
                attendanceViewHolder.tvStatus.setBackground(mContext.getResources().getDrawable(R.drawable.round_view_skin));
            }

        }
        // Loading fonts
        //        attendanceViewHolder.tvStatus.setTypeface(loadFont(mContext, FONT_DOSIS_MEDIUM));
        //        attendanceViewHolder.tvTotalWorkItem.setTypeface(loadFont(mContext, FONT_DOSIS_REGULAR));
        //        attendanceViewHolder.tvCheckInTime.setTypeface(loadFont(mContext, FONT_DOSIS_REGULAR));
        //        attendanceViewHolder.tvCheckOutTime.setTypeface(loadFont(mContext, FONT_DOSIS_REGULAR));
        //        attendanceViewHolder.tvWeekDay.setTypeface(loadFont(mContext, FONT_DOSIS_MEDIUM));
        //        attendanceViewHolder.tvDate.setTypeface(loadFont(mContext, FONT_DOSIS_MEDIUM));
    }

    @Override
    public int getItemCount() {
        if (mAttendanceList != null)
            return mAttendanceList.size();
        return 0;
    }

    public void swapAttendanceDataList(List<AttendanceSheetResponse> attendanceList) {
        Log.d(TAG_ATTENDANCE_ADAPTER, "swapDataList() has been instantiated");
        mAttendanceList = attendanceList;
        notifyDataSetChanged();
    }

    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_ATTENDANCE, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    String subtractDates(String checkOut, String checkIn) throws ParseException {
        Log.d(TAG_FRAG_ATTENDANCE, "subtractDates() has been instantiated");

        String totalTime = null;
        DateFormat originalFormat = new SimpleDateFormat(mContext.getString(R.string.original_date_format), Locale.ENGLISH);

        Date checkOutDate = originalFormat.parse(checkOut);
        Date checkInDate = originalFormat.parse(checkIn);

        long dateDifference = checkOutDate.getTime() - checkInDate.getTime();

        String diffHours = String.valueOf((int) (dateDifference / (60 * 60 * 1000)));
        String diffMins = String.valueOf((int) ((dateDifference / (60 * 1000)) % 60));

        totalTime = diffHours + "h " + (diffMins) + "m";

        return totalTime;
    }

    public String formatTime(String timeToFormat) throws ParseException {
        Log.d(TAG_FRAG_ATTENDANCE, "formatTime() has been instantiated");

        String pattern = mContext.getString(R.string.date_format);
        DateFormat formatter = new SimpleDateFormat(pattern);
        DateFormat originalFormat = new SimpleDateFormat(mContext.getString(R.string.original_date_format), Locale.ENGLISH);
        Date date = originalFormat.parse(timeToFormat);
        return formatter.format(date);
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_week_day)
        public TextView tvWeekDay;
        @BindView(R.id.tv_date)
        public TextView tvDate;
        @BindView(R.id.tv_total_work_item)
        public TextView tvTotalWorkItem;
        @BindView(R.id.tv_status)
        public TextView tvStatus;
        @BindView(R.id.tv_check_in_time)
        public TextView tvCheckInTime;
        @BindView(R.id.tv_check_out_time)
        public TextView tvCheckOutTime;
        @BindView(R.id.ll_work_time)
        public LinearLayout llWorkTime;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
