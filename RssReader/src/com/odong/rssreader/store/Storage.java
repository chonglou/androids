package com.odong.rssreader.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.odong.rssreader.Constants;
import com.odong.rssreader.utils.Rss;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 14-9-22.
 */
public class Storage {
    public synchronized static void setContext(Context context) {
        instance = new Storage(context);
    }

    public synchronized static Storage getInstance() {
        return instance;
    }

    private static Storage instance;

    private Storage(Context context) {
        helper = new DBHelper(context);
    }

    public void log(String message) {
        ContentValues cv = new ContentValues();
        cv.put("message", message);
        SQLiteDatabase db = getDb(true);
        db.insert("logs", null, cv);

    }

    public String getFeedUrl(int id) {
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("feeds", new String[]{"url"}, "id = ? ", new String[]{Integer.toString(id)}, null, null, "id DESC", "1");
        try {

            return cur.moveToFirst() ? cur.getString(0) : null;
        } finally {
            cur.close();
        }
    }

    public void setItemRead(int item) {
        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        cv.put("read", 1);
        db.update("items", cv, "id = ?", new String[]{Integer.toString(item)});
    }

    public int countUnreadItem(int feed) {
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM items WHERE read=0 AND feed = ?", new String[]{Integer.toString(feed)});
        try {
            cur.moveToFirst();
            return cur.getInt(0);
        } finally {
            cur.close();
        }
    }

    public Integer getItemId(String link) {
        SQLiteDatabase db = getDb(false);

        Cursor cur = db.query("items", new String[]{"id"}, "link = ? ", new String[]{link}, null, null, "id DESC", "1");
        try {

            return cur.moveToFirst() ? cur.getInt(0) : null;
        } finally {
            cur.close();

        }
    }

    public interface FeedCallback {
        void run(int id, String url, String title, String description, Date lastSync);
    }

    public interface ItemCallback {
        void run(int id, String link, String title, String description, String pubDate, boolean read);
    }

    public void listItem(int feed, int offset, int page, ItemCallback callback) {
        if (offset == 0) {
            offset = 1024 * 1024 * 10;
        }
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("items", new String[]{"id", "link", "title", "description", "pubDate", "read"}, "feed = ? AND id < ?", new String[]{Integer.toString(feed), Integer.toString(offset)}, null, null, "id DESC", Integer.toString(page));
        try {

            while (cur.moveToNext()) {
                callback.run(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getInt(5) == 1);
            }
        } finally {
            cur.close();

        }
    }

    public void listFeed(FeedCallback callback) {
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("feeds", new String[]{"id", "url", "title", "description", "lastSync"}, null, null, null, null, "id DESC");
        try {
            while (cur.moveToNext()) {
                callback.run(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), Constants.dateFormat.parse(cur.getString(4)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            cur.close();

        }
    }

    public void addItems(int feed, List<Rss.Item> items) {
        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        for (Rss.Item i : items) {
            if (getItemId(i.link) == null) {
                cv.put("link", i.link);
                cv.put("title", i.title);
                cv.put("description", i.description);
                cv.put("pubDate", i.pubDate);
                cv.put("feed", feed);
                db.insert("items", null, cv);
            }
        }

        cv.clear();
        cv.put("lastSync", Constants.dateFormat.format(new Date()));
        db.update("feeds", cv, "id = ?", new String[]{Integer.toString(feed)});

    }

    public Integer getFeedId(String url) {
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("feeds", new String[]{"id"}, "url = ? ", new String[]{url}, null, null, "id DESC", "1");
        try {
            return cur.moveToFirst() ? cur.getInt(0) : null;
        } finally {
            cur.close();

        }
    }

    public void delFeed(int id) {
        SQLiteDatabase db = getDb(true);
        db.delete("feeds", "id = ? ", new String[]{Integer.toString(id)});
        db.delete("items", "feed = ? ", new String[]{Integer.toString(id)});

    }

    public int addFeed(Rss.Channel channel) {
        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        cv.put("title", channel.title);
        cv.put("description", channel.description);
        cv.put("lastSync", Constants.dateFormat.format(new Date()));

        Integer id = getFeedId(channel.url);
        if (id == null) {
            cv.put("url", channel.url);
            id = (int) db.insert("feeds", null, cv);
        } else {
            db.update("feeds", cv, "url = ?", new String[]{channel.url});
        }

        return id;
    }

    public void set(String key, String val) {
        SQLiteDatabase db = getDb(true);
        ContentValues cv = new ContentValues();
        cv.put("val", val);
        if (get(key) == null) {

            cv.put("`key`", key);

            db.insert("settings", null, cv);
        } else {
            db.update("settings", cv, "`key` = ?", new String[]{key});
        }

    }

    public String get(String key) {
        SQLiteDatabase db = getDb(false);
        Cursor cur = db.query("settings", new String[]{"val"}, "`key` = ? ", new String[]{key}, null, null, "id DESC", "1");
        try {
            return cur.moveToFirst() ? cur.getString(0) : null;
        } finally {
            cur.close();

        }

    }

    private SQLiteDatabase getDb(boolean writable) {
        return writable ? helper.getWritableDatabase() : helper.getReadableDatabase();
    }

    private final DBHelper helper;
}
