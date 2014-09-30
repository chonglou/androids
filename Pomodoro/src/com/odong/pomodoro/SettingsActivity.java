package com.odong.pomodoro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by flamen on 14-9-30.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        initSpinner(R.id.sp_settings_tasks, R.array.sp_items_settings_tasks, Constants.KEY_TASK_COUNTER, 1);
        initSpinner(R.id.sp_settings_timer, R.array.sp_items_settings_timer, Constants.KEY_TASK_TIMER, 1);
        initSpinner(R.id.sp_settings_short_break, R.array.sp_items_settings_short_break, Constants.KEY_TASK_SHORT_BREAK, 1);
        initSpinner(R.id.sp_settings_longer_break, R.array.sp_items_settings_longer_break, Constants.KEY_TASK_LONGER_BREAK, 1);
    }


    private void initSpinner(int id, int items, final String key, int def) {
        Spinner keep = (Spinner) findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        keep.setAdapter(adapter);

        keep.setSelection(getPreferences(0).getInt(key, def));

        keep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = getPreferences(0).edit();
                editor.putInt(key, position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}