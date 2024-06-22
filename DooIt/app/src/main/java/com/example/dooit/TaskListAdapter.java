package com.example.dooit;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskHolder> {


    List<TaskModel> taskList;
    Activity activity;
    String status;


    public TaskListAdapter(List<TaskModel> taskList, Activity activity, String status) {
        this.taskList = taskList;
        this.activity = activity;
        this.status = status;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_list_layout, parent, false);
        return new TaskHolder(view, taskList, activity, status);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        holder.taskCheckBox.setText(taskList.get(position).getDesc());
        if (taskList.get(position).getStatus()) {
            holder.taskCheckBox.setChecked(true);
            holder.taskCheckBox.setPaintFlags(holder.taskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.taskCheckBox.setChecked(false);
            holder.taskCheckBox.setPaintFlags(holder.taskCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        CheckBox taskCheckBox;
        List<TaskModel> taskList;
        Activity activity;
        Button deleteBtn;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor  editor;
        Boolean status = false;
        List<TodoModel> todoList;
        String serializedTaskList;
        String count;

        @SuppressLint("NotifyDataSetChanged")
        public TaskHolder(@NonNull View itemView, List<TaskModel> taskList, Activity activity, String count) {
            super(itemView);
            this.taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
            this.deleteBtn = itemView.findViewById(R.id.taskDeleteBtn);
            this.taskList = taskList;
            this.activity = activity;
            this.count = count;
            sharedPreferences = activity.getSharedPreferences("myData", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            serializedTaskList = sharedPreferences.getString("todoList", null);
            todoList = ArrayListConverter.toArrayList(serializedTaskList);

            taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    status = true;
                    taskCheckBox.setPaintFlags(taskCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    status = false;
                    taskCheckBox.setPaintFlags(taskCheckBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
                taskList.get(getAdapterPosition()).setStatus(status);
                todoList.get(Integer.parseInt(count)).setTaskList(taskList);
                saveData();
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    taskList.remove(getAdapterPosition());
                    todoList.get(Integer.parseInt(count)).setTaskList(taskList);
                    saveData();
                    notifyDataSetChanged();
                }
            });

        }

        void saveData() {
            serializedTaskList = ArrayListConverter.fromArrayList((ArrayList<TodoModel>) todoList);
            editor.putString("todoList", serializedTaskList);
            editor.apply();
        }
    }
}
