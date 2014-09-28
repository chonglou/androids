package com.odong.rssreader.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by flamen on 14-9-22.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "RssReader.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE settings(id INTEGER PRIMARY KEY AUTOINCREMENT, `key` VARCHAR(255) NOT NULL, val TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX setting_key ON settings(`key`)");
        db.execSQL("CREATE TABLE feeds(id INTEGER PRIMARY KEY AUTOINCREMENT, link VARCHAR(255) NOT NULL, logo varchar(255), name varchar(255) NOT NULL, description varchar(500) NOT NULL, last_sync DATETIME, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE UNIQUE INDEX feed_link ON feeds(link)");
        db.execSQL("CREATE TABLE items(id INTEGER PRIMARY KEY AUTOINCREMENT, url VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL, body TEXT NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE UNIQUE INDEX item_url ON items(url)");
        db.execSQL("CREATE TABLE logs(id INTEGER PRIMARY KEY AUTOINCREMENT, message VARCHAR(255) NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(String name : new String[]{"logs", "settings", "feeds", "items"}) {
            db.execSQL("DROP TABLE IF EXISTS " + name);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
