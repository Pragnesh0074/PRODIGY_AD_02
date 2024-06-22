package com.example.dooit;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.TodoHolder> {

    List<TodoModel> todoList;
    Activity activity;
    OnTodoListChangedListener onTodoListChangedListener;

    public ToDoListAdapter(List<TodoModel> todoList, Activity activity, OnTodoListChangedListener listener) {
        this.todoList = todoList;
        this.activity = activity;
        this.onTodoListChangedListener = listener;
    }

    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.todo_list_layout, parent, false);
        return new ToDoListAdapter.TodoHolder(view, todoList, activity, onTodoListChangedListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {
        holder.name.setText(todoList.get(position).getTitle());
        holder.status.setText("Category :- "+todoList.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class TodoHolder extends RecyclerView.ViewHolder {

        TextView name, status;
        Button deleteBtn;
        List<TodoModel> todoList;
        Activity activity;
        ConstraintLayout todoBox;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor  editor;
        String serializedTaskList;
        OnTodoListChangedListener onTodoListChangedListener;

        public TodoHolder(@NonNull View itemView, List<TodoModel> todoList, Activity activity, OnTodoListChangedListener listener) {
            super(itemView);
            this.todoList = todoList;
            this.activity = activity;
            this.onTodoListChangedListener = listener;
            name = itemView.findViewById(R.id.todoName);
            status = itemView.findViewById(R.id.todoStatus);
            deleteBtn = itemView.findViewById(R.id.todoDeleteBtn);
            todoBox = itemView.findViewById(R.id.todoBox);
            sharedPreferences = activity.getSharedPreferences("myData", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            todoBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity, AddTaskActivity.class);
                    i.putExtra("status", String.valueOf(getAdapterPosition()));
                    activity.startActivity(i);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    todoList.remove(getAdapterPosition());
                    serializedTaskList = ArrayListConverter.fromArrayList((ArrayList<TodoModel>) todoList);
                    editor.putString("todoList", serializedTaskList);
                    editor.apply();
                    notifyDataSetChanged();
                    onTodoListChangedListener.onTodoListChanged();
                }
            });
        }
    }
}
