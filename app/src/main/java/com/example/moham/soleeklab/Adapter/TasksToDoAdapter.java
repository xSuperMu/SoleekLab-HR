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

import static com.example.moham.soleeklab.Utils.Constants.TAG_TASKS_TODO_ADAPTER;

public class TasksToDoAdapter extends RecyclerView.Adapter<TasksToDoAdapter.TaskToDoViewHolder> {
    private Context mContext;
    private List<Task> mTaskToDoList;

    public TasksToDoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TaskToDoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG_TASKS_TODO_ADAPTER, "onCreateViewHolder() has been instantiated");
        View taskToDoListItem = LayoutInflater.from(mContext).inflate(R.layout.task_todo_list_item, viewGroup, false);

        return new TaskToDoViewHolder(taskToDoListItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskToDoViewHolder taskToDoViewHolder, int i) {
        Log.d(TAG_TASKS_TODO_ADAPTER, "onBindViewHolder() has been instantiated");
    }

    @Override
    public int getItemCount() {
        return mTaskToDoList.size();
    }

    class TaskToDoViewHolder extends RecyclerView.ViewHolder {

        public TaskToDoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
