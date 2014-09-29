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

    public static void alert(Context context, String message) {

        new AlertDialog.Builder(context).setTitle(R.string.dlg_error_title)
                .setMessage(message)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public static final String SUCCESS = "success";
    public static final int ITEM_PAGE = 20;
    public static final int[] SETTINGS_REFRESH_ITEMS = {10 * 60, 30 * 60, 60 * 60, 6 * 60 * 60, 12 * 60 * 60, 24 * 60 * 60};
    public static final int[] SETTINGS_KEEP_ITEMS = {50, 200, 500, 1000};
}
