package com.example.dooit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddTaskActivity extends AppCompatActivity {

    EditText title, task;
    Button addTaskBtn;
    Spinner labelSpinner;
    RecyclerView taskList;
    String titleText, taskText, labelText = "";
    List<String> labelsList = new ArrayList<>(Arrays.asList("Personal", "Work", "Finance", "Other"));
    List<TaskModel> taskModelList;
    TaskModel newTask;
    TodoModel newTodo;
    TaskListAdapter taskListAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor  editor;
    List<TodoModel> todoList;
    Intent i;
    String status;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));

        title = findViewById(R.id.titleInput);
        task = findViewById(R.id.taskInput);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        labelSpinner = findViewById(R.id.labelSpinner);
        taskList = findViewById(R.id.taskList);
        i = getIntent();
        status = i.getStringExtra("status");
        sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String serializedTaskList = sharedPreferences.getString("todoList", null);
        if(Objects.equals(status, "new")) {
            if (serializedTaskList != null) {
                todoList = ArrayListConverter.toArrayList(serializedTaskList);
            }
            else {
                todoList = new ArrayList<>();
            }
            taskModelList = new ArrayList<>();
            newTodo = new TodoModel(titleText, labelText, taskModelList);
            todoList.add(newTodo);
            status = String.valueOf(todoList.size()-1);
            title.setText("");
            task.setText("");
            labelSpinner.setSelection(0);
        }
        else {
            todoList = ArrayListConverter.toArrayList(serializedTaskList);
            taskModelList = todoList.get(Integer.parseInt(status)).getTaskList();
            title.setText(todoList.get(Integer.parseInt(status)).getTitle());
            labelText = todoList.get(Integer.parseInt(status)).getLabel();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labelsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        labelSpinner.setAdapter(adapter);

        taskListAdapter = new TaskListAdapter(taskModelList, AddTaskActivity.this, status);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(taskListAdapter);

        labelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                labelText = labelsList.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                taskText = task.getText().toString();
                titleText = title.getText().toString();
                if (taskText.isEmpty() || titleText.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    newTask = new TaskModel(taskText, false);
                    taskModelList.add(newTask);
                    taskListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    void saveData() {
        if(!taskModelList.isEmpty()) {
            todoList.get(Integer.parseInt(status)).setTitle(title.getText().toString());
            todoList.get(Integer.parseInt(status)).setLabel(labelText);
            todoList.get(Integer.parseInt(status)).setTaskList(taskModelList);
            String serializedTaskList = ArrayListConverter.fromArrayList((ArrayList<TodoModel>) todoList);
            editor.putString("todoList", serializedTaskList);
            editor.apply();
            task.setText("");
        }
    }
}