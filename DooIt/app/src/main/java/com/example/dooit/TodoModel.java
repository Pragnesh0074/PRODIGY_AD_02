package com.example.dooit;

import java.util.List;

public class TodoModel {

    String title, label;
    List<TaskModel> taskList;

    public TodoModel(String title, String label, List<TaskModel> taskList) {
        this.title = title;
        this.label = label;
        this.taskList = taskList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TaskModel> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskModel> taskList) {
        this.taskList = taskList;
    }
}
