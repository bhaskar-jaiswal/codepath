package com.example.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.constants.Priority;
import com.example.constants.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhaskarjaiswal on 6/9/16.
 */
public class TasksDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
                                    MySQLiteHelper.TASK_NAME,
                                    MySQLiteHelper.NOTES,
                                    MySQLiteHelper.DUEDATE,
                                    MySQLiteHelper.PRIORITY,
                                    MySQLiteHelper.STATUS};

    public TasksDataSource(Context context) {
        dbHelper = MySQLiteHelper.getInstance(context);
    }

    public void open() throws SQLException {
        if(database == null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
    }

    /*
    * Change parameters to Tasks object
    */
    public void createTask(Tasks task) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TASK_NAME, task.getTaskName());
        values.put(MySQLiteHelper.NOTES, task.getNotes());
        values.put(MySQLiteHelper.DUEDATE, task.getDueDate());
        values.put(MySQLiteHelper.PRIORITY, task.getPriority().toString());
        values.put(MySQLiteHelper.STATUS, task.getStatus().toString());

        long insertId = database.insert(MySQLiteHelper.TABLE_TASKS, null, values);
        /*Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);*/
//        cursor.moveToFirst();
//        Tasks task = cursorToTasks(cursor);
//        cursor.close();
    }
    public void deleteComment(Long id) {
//        long id = task.getId();
        System.out.println("Task deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_TASKS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Tasks> getAllTasks() {
        List<Tasks> listTasks = new ArrayList<Tasks>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tasks task = cursorToTasks(cursor);
            listTasks.add(task);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return listTasks;
    }

    public Tasks getTask(String id) {

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASKS,
                allColumns, "_id=?", new String[]{id}, null, null, null);

        Tasks task = null;
        cursor.moveToFirst();
//        System.out.println("Count is: "+cursor.getCount());

        if(cursor.getCount()>0){
            task = cursorToTasks(cursor);
        }

        // make sure to close the cursor
        cursor.close();
        return task;
    }

    public void updateTask(Tasks task){

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TASK_NAME, task.getTaskName());
        values.put(MySQLiteHelper.NOTES,task.getNotes());
        values.put(MySQLiteHelper.DUEDATE, task.getDueDate());
        values.put(MySQLiteHelper.PRIORITY, task.getPriority().toString());
        values.put(MySQLiteHelper.STATUS, task.getStatus().toString());

        database.update(MySQLiteHelper.TABLE_TASKS, values, MySQLiteHelper.COLUMN_ID+"=?", new String[]{String.valueOf(task.getId())});
    }

    private Tasks cursorToTasks(Cursor cursor) {
        Tasks task = new Tasks();
        task.setId(cursor.getLong(0));
        task.setTaskName(cursor.getString(1));
        task.setNotes(cursor.getString(2));
        task.setDueDate(cursor.getString(3));

        Log.d("DueDate", task.getDueDate());

        task.setPriority(Priority.valueOf(cursor.getString(4)));
        task.setStatus(Status.valueOf(cursor.getString(5)));
        return task;
    }
}
