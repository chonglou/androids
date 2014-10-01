package com.odong.pomodoro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_help:
                onMessage(R.string.help_title, R.string.help_body, R.drawable.ic_action_about);
                break;
            case R.id.action_about:
                onMessage(R.string.about_title, R.string.about_body, R.drawable.ic_action_help);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setStatus();
        onRefresh();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dlg_exit_message)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.btn_no, null)
                .show();

    }

    @Override
    protected void onResume() {
        setStatus();
        super.onResume();
    }

    private void onMessage(int title, int body, int icon) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("icon", icon);
        startActivity(intent);
    }

    private void setTextViewText(int id, int message, Object...args){
        TextView status = (TextView)findViewById(id);
        String text = String.format(getResources().getString(message),args);
        status.setText(Html.fromHtml(text));
    }

    private void setStatus(){
        SharedPreferences sp = getSharedPreferences(Constants.STORAGE_SETTINGS_NAME, 0);
        setTextViewText(R.id.tv_main_settings, R.string.lbl_current_settings,
                Constants.ITEMS_TASK_COUNTER[sp.getInt(Constants.KEY_TASK_COUNTER, 1)],
                Constants.ITEMS_TASK_TIMER[sp.getInt(Constants.KEY_TASK_TIMER, 1)],
                Constants.ITEMS_TASK_SHORT_BREAK[sp.getInt(Constants.KEY_TASK_SHORT_BREAK, 1)],
                Constants.ITEMS_TASK_LONGER_BREAK[sp.getInt(Constants.KEY_TASK_LONGER_BREAK, 1)]
                );
    }

    private void onRefresh(){
        setTextViewText(R.id.tv_main_next_event, R.string.lbl_next_event, getString(R.string.lbl_task));
        setTextViewText(R.id.tv_main_clock, R.string.lbl_clock, 0, 42, 56);
    }

}
