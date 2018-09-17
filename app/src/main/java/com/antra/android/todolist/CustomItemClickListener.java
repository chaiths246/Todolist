package com.antra.android.todolist;

import android.view.View;

public interface CustomItemClickListener {
    void onNameClick(View v, int position,String itemname);
    void onEditClick(int position);
    void onDelClick(int position);

}