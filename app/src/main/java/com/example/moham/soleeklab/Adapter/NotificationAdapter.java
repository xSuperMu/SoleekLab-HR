package com.example.moham.soleeklab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moham.soleeklab.Model.Notification;
import com.example.moham.soleeklab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moham.soleeklab.Utils.Constants.TAG_NOTIFICATION_ADAPTER;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mContext;
    private List<Notification> mNotificationList;

    public NotificationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG_NOTIFICATION_ADAPTER, "onCreateViewHolder() has been instantiated");
        View notificationListItem = LayoutInflater.from(mContext).inflate(R.layout.notification_list_item, viewGroup, false);

        return new NotificationViewHolder(notificationListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder notificationViewHolder, int i) {
        Log.d(TAG_NOTIFICATION_ADAPTER, "onBindViewHolder() has been instantiated");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG_NOTIFICATION_ADAPTER, "getItemCount() has been instantiated");
        return mNotificationList.size();
    }

    private void swapNotificationList(List<Notification> notificationsList) {
        Log.d(TAG_NOTIFICATION_ADAPTER, "swapNotificationList() has been instantiated");
        mNotificationList = notificationsList;
        notifyDataSetChanged();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_notification_title)
        TextView tvNotificationTitle;
        @BindView(R.id.tv_notification_details)
        TextView tvNotificationDetails;
        @BindView(R.id.tv_notification_time)
        TextView tvNotificationTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
