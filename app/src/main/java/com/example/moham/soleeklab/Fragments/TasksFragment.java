package com.example.moham.soleeklab.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moham.soleeklab.Activities.HomeActivity;
import com.example.moham.soleeklab.Interfaces.TasksFragInterface;
import com.example.moham.soleeklab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_BOLD;
import static com.example.moham.soleeklab.Utils.Constants.FONT_DOSIS_MEDIUM;
import static com.example.moham.soleeklab.Utils.Constants.INT_FRAGMENT_TASKS_POS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_DONE;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_TASKS;
import static com.example.moham.soleeklab.Utils.Constants.TAG_FRAG_TODO;

public class TasksFragment extends Fragment implements TasksFragInterface {

    HomeActivity mHomeActivity;
    @BindView(R.id.tv_action_bar_tasks)
    TextView tvActionBarTasks;
    @BindView(R.id.iv_no_tasks_icon)
    ImageView ivNoTasksIcon;
    @BindView(R.id.tv_no_tasks_text)
    TextView tvNoTasksText;
    @BindView(R.id.cl_no_tasks)
    ConstraintLayout clNoTasks;
    @BindView(R.id.tv_task_todo)
    TextView tvTaskTodo;
    @BindView(R.id.tv_task_done)
    TextView tvTaskDone;
    @BindView(R.id.ll_tasks_header)
    LinearLayout llTasksHeader;
    @BindView(R.id.view_separator)
    View viewSeparator;
    @BindView(R.id.tasks_frame_fragment_holder)
    FrameLayout tasksFrameFragmentHolder;
    @BindView(R.id.cl_tasks)
    LinearLayout clTasks;
    Unbinder unbinder;

    public TasksFragment() {
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_FRAG_TASKS, "onCreateView() has been instantiated");

        mHomeActivity.bnvNavigation.getMenu().getItem(INT_FRAGMENT_TASKS_POS - 1).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        unbinder = ButterKnife.bind(this, view);

        instantiateViews();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG_FRAG_TASKS, "onAttach() has been instantiated");

        if (context instanceof Activity)
            mHomeActivity = (HomeActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_FRAG_TASKS, "onDestroyView() has been instantiated");
        unbinder.unbind();
    }

    @Override
    @OnClick(R.id.tv_task_todo)
    public void handleToDoClicked() {
        Log.d(TAG_FRAG_TASKS, "handleToDoClicked() has been instantiated");
        switchFragment(TaskToDoFragment.newInstance(), TAG_FRAG_TODO);
        tvTaskTodo.setBackground(getResources().getDrawable(R.drawable.task_todo_border));
        tvTaskTodo.setTextColor(getResources().getColor(R.color.colorWhite));
        tvTaskDone.setBackground(getResources().getDrawable(R.drawable.task_done_border_frame));
        tvTaskDone.setTextColor(getResources().getColor(R.color.colorBlue));
    }

    @Override
    @OnClick(R.id.tv_task_done)
    public void handleDoneClicked() {
        Log.d(TAG_FRAG_TASKS, "handleDoneClicked() has been instantiated");
        switchFragment(TaskDoneFragment.newInstance(), TAG_FRAG_DONE);
        tvTaskTodo.setBackground(getResources().getDrawable(R.drawable.task_todo_border_frame));
        tvTaskTodo.setTextColor(getResources().getColor(R.color.colorBlue));
        tvTaskDone.setBackground(getResources().getDrawable(R.drawable.task_done_border));
        tvTaskDone.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void instantiateViews() {
        Log.d(TAG_FRAG_TASKS, "instantiateViews() has been instantiated");

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.tasks_frame_fragment_holder, TaskToDoFragment.newInstance(), TAG_FRAG_TODO);
        transaction.commit();

        tvTaskTodo.setBackground(getResources().getDrawable(R.drawable.task_todo_border));
        tvTaskTodo.setTextColor(getResources().getColor(R.color.colorWhite));
        tvTaskDone.setBackground(getResources().getDrawable(R.drawable.task_done_border_frame));
        tvTaskDone.setTextColor(getResources().getColor(R.color.colorBlue));
        setFontsToViews();
    }

    @Override
    public void switchFragment(Fragment fragment, String tag) {
        Log.d(TAG_FRAG_TASKS, "switchFragment() has been instantiated");
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.tasks_frame_fragment_holder, fragment, tag);
        transaction.commit();
    }

    @Override
    public Typeface loadFont(Context context, String fontPath) {
        Log.d(TAG_FRAG_TASKS, "loadFont() has been instantiated");

        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public void setFontsToViews() {
        Log.d(TAG_FRAG_TASKS, "setFontsToViews() has been instantiated");
        tvActionBarTasks.setTypeface(loadFont(getActivity(), FONT_DOSIS_BOLD));
        tvTaskTodo.setTypeface(loadFont(getActivity(), FONT_DOSIS_BOLD));
        tvTaskDone.setTypeface(loadFont(getActivity(), FONT_DOSIS_BOLD));
        tvNoTasksText.setTypeface(loadFont(getActivity(), FONT_DOSIS_MEDIUM));
    }
}
