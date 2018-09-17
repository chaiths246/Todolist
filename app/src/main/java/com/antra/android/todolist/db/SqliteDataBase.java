package com.antra.android.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.antra.android.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class SqliteDataBase extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	2;
    private	static final String	DATABASE_NAME = "todolistdb";
    private	static final String TABLE_TODOLIST = "todolisttable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LIST = "list";
    private static final String COLUMN_TODOS = "todos";
    private static final String COLUMN_DONE = "done";// 1 --> done 0 --> not
    private static final String COLUMN_TODO_DESC = "todosdesc";
    private static final String COLUMN_TODO_TIME = "todostime";

    public SqliteDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_PRODUCTS_TABLE = "CREATE	TABLE " + TABLE_TODOLIST + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_LIST + " TEXT," + COLUMN_TODOS + " TEXT," + COLUMN_TODO_DESC +" TEXT,"+ COLUMN_TODO_TIME+" TEXT, "+ COLUMN_DONE +
                " INTEGER DEFAULT 0 )";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST);
        onCreate(db);
    }

    public List<Task> listProducts(){
        String sql = "select DISTINCT "+COLUMN_LIST+" from " + TABLE_TODOLIST;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> storeProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(0);
                storeProducts.add(new Task(name));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeProducts;
    }

    public List<Task> getTodoslist(String listname){
        String sql = "select "+COLUMN_TODOS+" from " + TABLE_TODOLIST +" where "+COLUMN_LIST+"='"+listname+"' " +
                "AND "+COLUMN_TODOS+" != 'null' AND "+COLUMN_DONE+" = 0" ;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> storeProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                String todo = cursor.getString(0);
                storeProducts.add(new Task(todo));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeProducts;
    }


    public List<Task> getDoneTodoslist(String listname){
        String sql = "select "+COLUMN_TODOS+" from " + TABLE_TODOLIST +" where "+COLUMN_LIST+"='"+listname+"' " +
                "AND "+COLUMN_TODOS+" != 'null' AND "+COLUMN_DONE+" = 1" ;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> storeProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                String todo = cursor.getString(0);
                storeProducts.add(new Task(todo));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeProducts;
    }

    public void addToDos(Task product,String listname,String timetodo){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOS, product.getName());
        values.put(COLUMN_LIST, listname);
        values.put(COLUMN_TODO_TIME, timetodo);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TODOLIST,null, values);
    }

    public long addList(Task product){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST, product.getName());
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_TODOLIST, null, values);
    }

    public void addWeekList() {
        String query = "INSERT INTO "+TABLE_TODOLIST+"("+COLUMN_LIST+","+COLUMN_TODOS+") VALUES" +
                " ('SUNDAY','YOGA'), ('MONDAY','YOGA'), ('TUESDAY','YOGA'), ('WEDNESDAY','YOGA'), ('THURSDAY','YOGA'), " +
                "('FRIDAY','YOGA'), ('SATURDAY','YOGA');";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public Task getListNameByID(int position){

        String query = "Select * FROM "	+ TABLE_TODOLIST + " WHERE " + COLUMN_ID + " = " + position;
        SQLiteDatabase db = this.getWritableDatabase();
        Task mProduct = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String listName = cursor.getString(1);
            mProduct = new Task(id, listName);
        }
        cursor.close();

        return mProduct;
    }

    public Task getListNameByName(String listname){

        String query = "Select * FROM "	+ TABLE_TODOLIST + " WHERE " + COLUMN_ID + " = '" + listname+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Task mProduct = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String listName = cursor.getString(1);
            mProduct = new Task(id, listName);
        }
        cursor.close();

        return mProduct;
    }

    public Task findProduct(String name){
        String query = "Select * FROM "	+ TABLE_TODOLIST + " WHERE " + COLUMN_LIST + " = " + "name";
        SQLiteDatabase db = this.getWritableDatabase();
        Task mProduct = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String productName = cursor.getString(1);
            mProduct = new Task(id, productName);
        }
        cursor.close();
        return mProduct;
    }
    public String getTodoDesc(String todolistname,String tododescname){
        String sqlquery = "select "+COLUMN_TODO_DESC+" from " + TABLE_TODOLIST +" where "+COLUMN_LIST+"='"+todolistname+"' " +
                "AND "+COLUMN_TODOS+" = '"+tododescname+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlquery,	null);
        if	(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        cursor.close();
        return "";
    }

    public String getTodoTime(String listname, String todoname) {
        String sqlquery = "select "+COLUMN_TODO_TIME+" from " + TABLE_TODOLIST +" where "+COLUMN_LIST+"='"+listname+"' " +
                "AND "+COLUMN_TODOS+" = '"+todoname+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlquery,	null);
        if	(cursor.moveToFirst()){
            return cursor.getString(0);
        }
        cursor.close();
        return "";
    }

    public int updateTodoDesc(String todolistname,String tododescname,String desc){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_DESC, desc);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_TODOLIST, values,COLUMN_LIST+"	= ? AND "+COLUMN_TODOS+" = ?", new String[] {todolistname,tododescname});
    }

    public int updateToDos(String listname,String updatedtodo, String todoname){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODOS, updatedtodo);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_TODOLIST, values,COLUMN_LIST+"	= ? AND "+COLUMN_TODOS+" = ?", new String[] {listname,todoname});
    }

    public int updateListName(String listname,String updatelistname){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST, updatelistname);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_TODOLIST, values, COLUMN_LIST	+ "	= ?", new String[] { String.valueOf(listname)});
    }

    public int updateTaskStatus(int taskstatus,String listname,String todoname){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DONE, taskstatus);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_TODOLIST, values,COLUMN_LIST+"	= ? AND "+COLUMN_TODOS+" = ?", new String[] {listname,todoname});
    }

    public int updateTodotime(String listname, String todoname, String todotime) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_TIME, todotime);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_TODOLIST, values,COLUMN_LIST+"	= ? AND "+COLUMN_TODOS+" = ?", new String[] {listname,todoname});
    }


    public int deleteToDos(String listname,String todo){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODOLIST, COLUMN_LIST+"	= ? AND "+COLUMN_TODOS+" = ?", new String[] {listname,todo});
    }

    public int deleteList(String listname){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TODOLIST, COLUMN_LIST	+ "	= ?", new String[] { String.valueOf(listname)});
    }


}


