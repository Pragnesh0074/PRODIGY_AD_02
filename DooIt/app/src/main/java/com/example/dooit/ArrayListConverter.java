package com.example.dooit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListConverter {
    private static Gson gson = new Gson();

    public static String fromArrayList(ArrayList<TodoModel> list) {
        return gson.toJson(list);
    }

    public static ArrayList<TodoModel> toArrayList(String data) {
        Type type = new TypeToken<ArrayList<TodoModel>>(){}.getType();
        return gson.fromJson(data, type);
    }
}
