package com.antra.android.todolist.adapter;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.antra.android.todolist.CustomTodosClickListener;
import com.antra.android.todolist.R;
import com.antra.android.todolist.db.SqliteDataBase;
import com.antra.android.todolist.model.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewListAddingAdapter  extends RecyclerView.Adapter<NewListAddingAdapter.MyViewHolder>{

    private Context context;
    private List<Task> listtodos;
    String listname;
    boolean istaskdone;
    CustomTodosClickListener listener;

    private SqliteDataBase mDatabase;

    public NewListAddingAdapter(Context context, List<Task> listtodos,String listname,boolean istaskdone,CustomTodosClickListener listener) {
        this.context = context;
        this.listtodos = listtodos;
        mDatabase = new SqliteDataBase(context);
        this.listname = listname;
        this.istaskdone = istaskdone;
        this.listener = listener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView deleteProduct;
        ImageView editProduct;
        CheckBox chktodo;
        TextView timeTodo;


        public MyViewHolder(View view) {
            super(view);

            name = (TextView) itemView.findViewById(R.id.name_todo);
            deleteProduct = (ImageView) itemView.findViewById(R.id.delete_todo);
            editProduct = (ImageView) itemView.findViewById(R.id.edit_todo);
            chktodo = itemView.findViewById(R.id.chechbox_todo);
            timeTodo = itemView.findViewById(R.id.time_todo);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newlistadding, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull NewListAddingAdapter.MyViewHolder myViewHolder, int position) {
        final String todoname = listtodos.get(position).getName();

        myViewHolder.name.setText(todoname);

        myViewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onTodoNameClick(todoname);
            }
        });

        myViewHolder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTodoEditClick(todoname);
            }
        });

        myViewHolder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTodoDeleteClick(todoname);
            }
        });

        String strtime = mDatabase.getTodoTime(listname,todoname);
        if(strtime != null){
            myViewHolder.timeTodo.setText(strtime);
        }else{
//            Date currentTime = Calendar.getInstance().getTime();
//            myViewHolder.timeTodo.setText(currentTime.getHours()+":"+currentTime.getMinutes());
        }
        myViewHolder.timeTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onTodotimeClick(myViewHolder.timeTodo,todoname);
            }
        });

        if(istaskdone){
            myViewHolder.chktodo.setChecked(true);
//            myViewHolder.timeTodo.setVisibility(View.GONE);
        }else{
            myViewHolder.chktodo.setChecked(false);
//            myViewHolder.timeTodo.setVisibility(View.VISIBLE);
        }
        myViewHolder.chktodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mDatabase.updateTaskStatus(1,listname,todoname);
                }else{
                    mDatabase.updateTaskStatus(0,listname,todoname);
                }
                refreshActivity();
            }
        });
    }

    private void refreshActivity() {
        ((Activity)context).finish();
        context.startActivity(((Activity) context).getIntent());
    }

    @Override
    public int getItemCount() {
        return listtodos.size();
    }
}
