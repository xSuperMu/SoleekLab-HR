package com.example.moham.soleeklab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moham.soleeklab.Model.Vacation;
import com.example.moham.soleeklab.R;

import java.util.List;

import static com.example.moham.soleeklab.Utils.Constants.TAG_VACATION_ADAPTER;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private Context mContext;
    private List<Vacation> mVacationList;

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
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
