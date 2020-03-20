package com.example.moham.soleeklab.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moham.soleeklab.Model.Task;
import com.example.moham.soleeklab.R;

import java.util.List;

import static com.example.moham.soleeklab.Utils.Constants.TAG_TASKS_DONE_ADAPTER;

public class TasksDoneAdapter extends RecyclerView.Adapter<TasksDoneAdapter.TasksDoneViewHolder> {

    private Context mContext;
    private List<Task> mTaskDoneList;
    public TasksDoneAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TasksDoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG_TASKS_DONE_ADAPTER, "onCreateViewHolder() has been instantiated");
        View taskDoneListItem = LayoutInflater.from(mContext).inflate(R.layout.task_done_list_item, viewGroup, false);

        return new TasksDoneViewHolder(taskDoneListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksDoneViewHolder tasksDoneViewHolder, int i) {
        Log.d(TAG_TASKS_DONE_ADAPTER, "onBindViewHolder() has been instantiated");

    }

    @Override
    public int getItemCount() {
        return mTaskDoneList.size();
    }

    class TasksDoneViewHolder extends RecyclerView.ViewHolder {
        public TasksDoneViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
