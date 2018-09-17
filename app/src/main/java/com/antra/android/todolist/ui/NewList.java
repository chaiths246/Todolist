package com.antra.android.todolist.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.antra.android.todolist.CustomTodosClickListener;
import com.antra.android.todolist.R;
import com.antra.android.todolist.adapter.NewListAddingAdapter;
import com.antra.android.todolist.db.SqliteDataBase;
import com.antra.android.todolist.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewList extends AppCompatActivity {
    Button btnaddList;
    String name = "";
    private NewListAddingAdapter donetodosAdapter;
    private RecyclerView productView;
    private RecyclerView completedList;
    EditText taskToDo;
    Button add;
    Button taskDone;
    ImageView imgAddPlus;
    EditText nameField;
    SqliteDataBase mDatabase;
    NewListAddingAdapter mAdapter;
    public String listname;
    List<Task> listtodos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            listname = getIntent().getExtras().getString("listname");
            setTitle(listname);
        }
//        RelativeLayout fLayout = (RelativeLayout) findViewById(R.id.activity_to_do);
        productView = (RecyclerView) findViewById(R.id.recycle2);
        completedList = (RecyclerView) findViewById(R.id.completed_todos);
        btnaddList = (Button) findViewById(R.id.add2);
        taskDone = (Button) findViewById(R.id.show_completed);
        imgAddPlus = findViewById(R.id.img_add_plus_id);
        mDatabase = new SqliteDataBase(this);
        loadToDosToAdapter();

        nameField = (EditText) findViewById(R.id.editListName);
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ObjectAnimator penObjectAnimator = ObjectAnimator.ofFloat(imgAddPlus , View.ROTATION,0f, 360f);
                penObjectAnimator.setDuration(500);
                penObjectAnimator.setInterpolator(new LinearInterpolator());
                penObjectAnimator.start();
            }
        });

        btnaddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDialog();
            }
        });

        taskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewList.this);
                completedList.setLayoutManager(linearLayoutManager);
                List<Task> donetodoslist = mDatabase.getDoneTodoslist(listname);
                donetodosAdapter = new NewListAddingAdapter(NewList.this, donetodoslist, listname, true, new CustomTodosClickListener() {

                    @Override
                    public void onTodoNameClick(String todoname) {

                    }

                    @Override
                    public void onTodoEditClick(String todoname) {
                        editTaskDialog(todoname);
                    }

                    @Override
                    public void onTodoDeleteClick(String todoname) {
                        showDelAlertDialog(todoname);
                    }

                    @Override
                    public void onTodotimeClick(TextView textView,String todoname) {

                    }
                });
                completedList.setAdapter(donetodosAdapter);

                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(NewList.this, R.anim.layout_animation_fall_down);
                completedList.setLayoutAnimation(animation);
            }
        });

    }

    private void loadToDosToAdapter() {
        listtodos.clear();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productView.setLayoutManager(linearLayoutManager);
        productView.setHasFixedSize(true);
        listtodos = mDatabase.getTodoslist(listname);

        if (listtodos.size() > 0) {
            productView.setVisibility(View.VISIBLE);
            mAdapter = new NewListAddingAdapter(this, listtodos, listname, false, new CustomTodosClickListener() {
                @Override
                public void onTodoNameClick(String todoname) {
                    Intent tododescIntent = new Intent(NewList.this, ToDoDesc.class);
                    tododescIntent.putExtra("tododescname", todoname);
                    tododescIntent.putExtra("todolistname", listname);
                    startActivity(tododescIntent);
                }

                @Override
                public void onTodoEditClick(String todoname) {
                    editTaskDialog(todoname);
                }

                @Override
                public void onTodoDeleteClick(String todoname) {
                    showDelAlertDialog(todoname);
                }

                @Override
                public void onTodotimeClick(final TextView textView,final String todoname) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(NewList.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String todotime = selectedHour + ":" + selectedMinute;
                            textView.setText( todotime);
                            mDatabase.updateTodotime(listname,todoname,todotime);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });
            productView.setAdapter(mAdapter);

        } else {
            productView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
    }

    private void addTaskDialog() {
        final String name = nameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(NewList.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
        } else {
            Task newProduct = new Task(name);
//            Date currentTime = Calendar.getInstance().getTime();
//            String timetodo = currentTime.getHours()+":"+currentTime.getMinutes();
            mDatabase.addToDos(newProduct, listname,null);
            //refresh the activity
//            finish();
//            startActivity(getIntent());
            loadToDosToAdapter();
            nameField.setText("");

        }
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                addTaskDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    private void editTaskDialog(final String todoName){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.activity_edittext, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.editListName);
        if(todoName != null){
            nameField.setText(todoName);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UPDATE TODO");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String updatedtodoname = nameField.getText().toString();
                if(TextUtils.isEmpty(updatedtodoname) ){
                    Toast.makeText(NewList.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    int updatenum = mDatabase.updateToDos(listname, updatedtodoname,todoName);
                    if(updatenum == 1){
                        Toast.makeText(NewList.this,"updated successfully",Toast.LENGTH_SHORT).show();
                    }
                    loadToDosToAdapter();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showDelAlertDialog(final String todoname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETE TODO");
        builder.setMessage("Do you want to delete "+todoname+"?");
        builder.create();
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int deletenum = mDatabase.deleteToDos(listname,todoname);
                if(deletenum == 1){
                    Toast.makeText(NewList.this,"deleted successfully",Toast.LENGTH_SHORT).show();
                }
                loadToDosToAdapter();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
