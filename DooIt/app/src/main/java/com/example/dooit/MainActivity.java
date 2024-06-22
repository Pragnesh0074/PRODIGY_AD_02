package com.example.dooit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoListChangedListener {

    Button addBtn, addFloatBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor  editor;
    List<TodoModel> todoList;
    RecyclerView todoListView;
    ImageView emptyImageView;
    TextView emptyTextView;
    ToDoListAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        checkForUpdate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));

        addBtn = findViewById(R.id.addBtn);
        addFloatBtn = findViewById(R.id.addFloatBtn);
        todoListView = findViewById(R.id.toDoList);
        emptyImageView = findViewById(R.id.imageView3);
        emptyTextView = findViewById(R.id.textView3);

        sharedPreferences = getSharedPreferences("myData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
                i.putExtra("status", "new");
                startActivity(i);
            }
        });

        addFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTaskActivity.class);
                i.putExtra("status", "new");
                startActivity(i);
            }
        });
    }

    void checkForUpdate() {
        String serializedTaskList = sharedPreferences.getString("todoList", null);
        if (serializedTaskList != null && !serializedTaskList.equals("[]")) {
            todoList = ArrayListConverter.toArrayList(serializedTaskList);
            todoListView.setVisibility(View.VISIBLE);
            addFloatBtn.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.INVISIBLE);
            addBtn.setVisibility(View.INVISIBLE);
            adapter =  new ToDoListAdapter(todoList, MainActivity.this, this);
            todoListView.setLayoutManager(new LinearLayoutManager(this));
            todoListView.setAdapter(adapter);
        } else {
            todoList = new ArrayList<>();
            emptyImageView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.VISIBLE);
            todoListView.setVisibility(View.INVISIBLE);
            addFloatBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onTodoListChanged() {
        checkForUpdate();
    }
}
