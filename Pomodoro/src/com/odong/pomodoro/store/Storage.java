package com.odong.pomodoro.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.odong.pomodoro.Constants;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by flamen on 14-9-30.
 */
public class Storage {
    public static synchronized Storage getInstance() {
        return instance;
    }

    public static synchronized void set(Context context) {
        instance = new Storage(context);
    }

    private static Storage instance;

    private Storage(Context context) {
        this.helper = new DBHelper(context);
    }

    //----------------------
    public interface TaskCallback {
        void run(int id, String title, String content, String todo);
    }

    public void delTask(int id){
        SQLiteDatabase db = getDb(true);
        db.delete("tasks", "id = ?", new String[]{Integer.toString(id)});
    }
    public void listTask(char status, int offset, int page, TaskCallback callback) {
        if (offset == 0) {
            offset = 1024 * 1024 * 10;
        }
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("tasks", new String[]{"id", "title", "content", "todo"}, "id < ? AND status= ?", new String[]{Integer.toString(offset), Character.toString(status)}, null, null, "id DESC", Integer.toString(page));
        try {
            while (cur.moveToNext()) {
                callback.run(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3));
            }
        } finally {
            cur.close();
        }
    }

    public void setTask(int id, char status) {

        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        cv.put("status", Character.toString(status));
        db.update("tasks", cv, "id = ?", new String[]{Integer.toString(id)});
    }

    public void setTask(int id, String title, String content, String todo) {
        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        cv.put("todo", todo);
        db.update("tasks", cv, "id = ?", new String[]{Integer.toString(id)});
    }

    public void addTask(String title, String content, String todo) {

        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("content", content);
        cv.put("todo", todo);
        SQLiteDatabase db = getDb(true);
        db.insert("tasks", null, cv);

    }

    public interface LogCallback {
        void run(int id, String message, Date created);
    }

    public void listLog(int offset, int page, LogCallback callback) {
        if (offset == 0) {
            offset = 1024 * 1024 * 10;
        }
        SQLiteDatabase db = getDb(false);

        Cursor cur = db.query("logs", new String[]{"id", "message", "created"}, "id < ?", new String[]{Integer.toString(offset)}, null, null, "id DESC", Integer.toString(page));
        try {
            while (cur.moveToNext()) {
                callback.run(cur.getInt(0), cur.getString(1), Constants.dateFormat.parse(cur.getString(2)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cur.close();
        }
    }

    public void log(String message) {
        ContentValues cv = new ContentValues();
        cv.put("message", message);
        SQLiteDatabase db = getDb(true);
        db.insert("logs", null, cv);

    }

    //----------------------
    private SQLiteDatabase getDb(boolean writable) {
        return writable ? helper.getWritableDatabase() : helper.getReadableDatabase();
    }


    private final DBHelper helper;


}
