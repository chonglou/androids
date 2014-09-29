package com.odong.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.odong.rssreader.store.Storage;

/**
 * Created by flamen on 14-9-27.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        initRefresh();
        initKeep();
        initNotice();

    }

    private void initKeep() {
        Spinner keep = (Spinner) findViewById(R.id.sp_settings_keep);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sp_items_settings_keep, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        keep.setAdapter(adapter);

        String sel = Storage.getInstance().get("feed.keep.index");
        keep.setSelection(sel == null ? adapter.getCount() - 1 : Integer.parseInt(sel));

        keep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Storage.getInstance().set("feed.keep.index", Integer.toString(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRefresh() {
        Spinner refresh = (Spinner) findViewById(R.id.sp_settings_refresh);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sp_items_settings_refresh, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        refresh.setAdapter(adapter);

        String sel = Storage.getInstance().get("refresh.space.index");
        refresh.setSelection(sel == null ? adapter.getCount() - 1 : Integer.parseInt(sel));

        refresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Storage.getInstance().set("refresh.space.index", Integer.toString(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initNotice() {
        Switch notice = (Switch) findViewById(R.id.sw_setting_notice);

        String val = Storage.getInstance().get("notice.enable");
        notice.setChecked(val == null || Boolean.parseBoolean(val));

        notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Storage.getInstance().set("notice.enable", Boolean.toString(isChecked));
            }
        });
    }


}