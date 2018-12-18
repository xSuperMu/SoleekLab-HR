package com.example.moham.soleeklab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moham.soleeklab.Model.Attendance;
import com.example.moham.soleeklab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_ATTENDANCE_ADAPTER;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private Context mContext;
    private List<Attendance> mAttendanceList;

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
    }

    @Override
    public int getItemCount() {
        return mAttendanceList.size();
    }

    void swapAttendanceDataList(List<Attendance> attendanceList) {
        Log.d(TAG_ATTENDANCE_ADAPTER, "swapDataList() has been instantiated");
        mAttendanceList = attendanceList;
        notifyDataSetChanged();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_week_day)
        TextView tvWeekDay;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_total_work_item)
        TextView tvTotalWorkItem;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.tv_check_in_time)
        TextView tvCheckInTime;
        @BindView(R.id.tv_check_out_time)
        TextView tvCheckOutTime;
        @BindView(R.id.ll_work_time)
        LinearLayout llWorkTime;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
