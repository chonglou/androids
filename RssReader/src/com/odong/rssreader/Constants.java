package com.odong.rssreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by flamen on 14-9-27.
 */
public class Constants {
    private Constants() {
    }

    public static String now() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static final int[] SETTINGS_REFRESH_ITEMS = {10 * 60, 30 * 60, 60 * 60, 6 * 60 * 60, 12 * 60 * 60, 24 * 60 * 60};
    public static final int[] SETTINGS_KEEP_ITEMS = {50, 200, 500, 1000};
}
