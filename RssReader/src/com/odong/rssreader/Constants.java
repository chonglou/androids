package com.odong.rssreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;

/**
 * Created by flamen on 14-9-27.
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

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String SUCCESS = "success";
    public static final int ITEM_PAGE = 20;
}
