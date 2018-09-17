package com.antra.android.todolist.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.antra.android.todolist.CustomItemClickListener;
import com.antra.android.todolist.R;
import com.antra.android.todolist.adapter.TodoListAdapter;
import com.antra.android.todolist.db.SqliteDataBase;
import com.antra.android.todolist.model.Task;
import com.antra.android.todolist.model.UserModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final List<String> todo_list = new ArrayList<>();
    private RecyclerView recyclerView,completedlistview;
    private TodoListAdapter mAdapter;
    private Button createList;
    static String newString = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    String list_name;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    private FirebaseAuth firebaseAuth;

    //    user
    private FirebaseUser user;


    //    auth state listener
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final int RC_SIGN_IN = 123;
    private int todolistNumId;
    SqliteDataBase mDatabase;
    TextView txtNolistmsg;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        createList = (Button) findViewById(R.id.createList);
        txtNolistmsg = findViewById(R.id.txt_nolist_msg_id);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = new SqliteDataBase(this);
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = firebaseDatabase.getReference("users");
        completedlistview=(RecyclerView)findViewById(R.id.completed_todos);

//        createList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createListDialog();
//                Intent listNameIntent = new Intent(MainActivity.this, ListNamingActivity.class);
//                startActivity(listNameIntent);
//                finish();
//            }
//        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // get user detail
                    String id = user.getUid();
                    String name = user.getDisplayName();
                    String email = user.getEmail();

                    String imgURL = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;

                    //create model
                    UserModel userModel = new UserModel(id, name, email, imgURL);

                    //add to firebase database
                    userRef.child(userModel.getId()).setValue(userModel);
                    userRef.push().setValue(userModel);

                } else {
//                    StartSignIn();
                }
            }
        };


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent inboxActivity = new Intent(MainActivity.this, InboxActivity.class);
//                startActivity(inboxActivity);
            }
        });

        loadListToAdapter();
    }

    void loadListToAdapter() {
        todo_list.clear();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Task> lists = mDatabase.listProducts();
        if(lists.size()>0){
            txtNolistmsg.setVisibility(View.GONE);
            for(int i = 0; i < lists.size();i++){
                todo_list.add(lists.get(i).getName());
            }
        }else{
            txtNolistmsg.setVisibility(View.VISIBLE);
        }

        mAdapter = new TodoListAdapter(this, todo_list, new CustomItemClickListener() {
            @Override
            public void onNameClick(View v, int position,String listname) {

                Log.e(TAG, "clicked position:" + position);
                Intent todosIntent = new Intent(MainActivity.this, NewList.class);
                todosIntent.putExtra("listname", listname);
                startActivity(todosIntent);
            }

            @Override
            public void onEditClick(int position) {
                updateListNameDialog(todo_list.get(position));
            }

            @Override
            public void onDelClick(int position) {
               showDelAlertDialog(todo_list.get(position));
            }

        });
        recyclerView.setAdapter(mAdapter);
    }

    private void StartSignIn() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }





    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_list:
                createListDialog();
                return true;
            case R.id.menu_add_week_list:
                createWeekList();
                loadListToAdapter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createWeekList() {
        mDatabase.addWeekList();
    }

    private void StartSignOut() {

//        AuthUI.getInstance()
//                .signOut(MainActivity.this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(MainActivity.this, "Succesfully Signed Out", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });

    }

    @Override
    protected void onPause() {
        super.onPause();

        firebaseAuth.removeAuthStateListener(authStateListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth.addAuthStateListener(authStateListener);

    }

    private void createListDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.activity_edittext, null);
        final EditText nameField = (EditText)subView.findViewById(R.id.editListName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CREATE LIST");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String listname = nameField.getText().toString();


                if(TextUtils.isEmpty(listname) ){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    long noOfList = mDatabase.addList(new Task(listname));
                    if(noOfList == 1){
                        Toast.makeText(MainActivity.this,"created successfully",Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    //refresh activity
//                    finish();
//                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    loadListToAdapter();
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

    private void updateListNameDialog(String name){
        final String listName = name;
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.activity_edittext, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.editListName);

        if(listName != null){
            nameField.setText(listName);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UPDATE LIST");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String updatedlistname = nameField.getText().toString();
                if(TextUtils.isEmpty(updatedlistname) ){
                    Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    int updatenum = mDatabase.updateListName(listName, updatedlistname);
                    if(updatenum == 1){
                        Toast.makeText(MainActivity.this,"updated successfully",Toast.LENGTH_SHORT).show();
                    }
                    loadListToAdapter();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    private void showDelAlertDialog(final String listname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CREATE LIST");
        builder.setMessage("Do you want to delete "+listname+"?");
        builder.create();
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int deletenum = mDatabase.deleteList(listname);
                if(deletenum >= 1){
                    Toast.makeText(MainActivity.this,"deleted successfully",Toast.LENGTH_SHORT).show();
                }
                loadListToAdapter();
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

