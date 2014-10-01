package com.odong.pomodoro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by flamen on 14-9-30.
 */
public class Constants {
    private Constants() {
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


    public final static String STORAGE_SETTINGS_NAME = "settings";
    public final static String KEY_TASK_COUNTER = "task.counter";
    public final static String KEY_TASK_TIMER = "task.timer";
    public final static String KEY_TASK_SHORT_BREAK = "task.short_break";
    public final static String KEY_TASK_LONGER_BREAK = "task.longer_break";

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
