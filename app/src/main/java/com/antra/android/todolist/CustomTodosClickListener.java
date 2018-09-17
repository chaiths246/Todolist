package com.antra.android.todolist;

import android.widget.TextView;

public interface CustomTodosClickListener {
    void onTodoNameClick(String todoname);
    void onTodoEditClick(String todoname);
    void onTodoDeleteClick(String todoname);
    void onTodotimeClick(TextView textView,String todoname);
}
