package com.antra.android.todolist.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.antra.android.todolist.R;
import com.antra.android.todolist.db.SqliteDataBase;

public class ToDoDesc extends AppCompatActivity {
    String tododescname;
    String todolistname;
    SqliteDataBase mDatabase;
    String strtododesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_desc);
        mDatabase = new SqliteDataBase(this);
        final EditText tododesc = findViewById(R.id.edt_tododesc_id);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            tododescname = getIntent().getExtras().getString("tododescname");
            todolistname = getIntent().getExtras().getString("todolistname");
            setTitle(tododescname);
        }

        //get desc
        String desc = mDatabase.getTodoDesc(todolistname,tododescname);
        tododesc.setText(desc);
        tododesc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                strtododesc = tododesc.getText().toString();
//                mDatabase.updateTodoDesc(todolistname,tododescname,strtododesc);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        mDatabase.updateTodoDesc(todolistname,tododescname,strtododesc);
        super.onBackPressed();
    }
}
