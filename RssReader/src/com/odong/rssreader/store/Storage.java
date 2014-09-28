package com.odong.rssreader.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONObject;

/**
 * Created by flamen on 14-9-22.
 */
public class Storage {
    public Storage(Context context) {
        helper = new DBHelper(context);
    }

    public void log(String message) {
        ContentValues cv = new ContentValues();
        cv.put("message", message);
        getDb(true).insert("logs", null, cv);
    }

    public void set(String key, String val){
        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        cv.put("val", val);
        if(get(key) == null){

            cv.put("`key`", key);

            db.insert("settings", null, cv);
        }
        else {
            db.update("settings", cv, "`key` = ?", new String[]{key});
        }
    }
    public String get(String key){
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("settings", new String[]{"val"}, "`key` = ? ", new String[]{key}, null, null, "id DESC");
        return cur.moveToFirst() ? cur.getString(0) : null;

    }

    private SQLiteDatabase getDb(boolean writable) {
        return writable ? helper.getWritableDatabase() : helper.getReadableDatabase();
    }

    private final DBHelper helper;
}
