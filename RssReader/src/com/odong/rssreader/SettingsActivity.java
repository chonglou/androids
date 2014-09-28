package com.odong.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.odong.rssreader.store.Storage;

/**
 * Created by flamen on 14-9-27.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initRefresh();
        initKeep();

    }

    private void initKeep() {
        Spinner keep = (Spinner) findViewById(R.id.sp_settings_keep);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sp_items_settings_keep, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        keep.setAdapter(adapter);

        String sel = new Storage(getApplicationContext()).get("feed.keep.index");
        keep.setSelection(sel == null ? adapter.getCount() - 1 : Integer.parseInt(sel));

        keep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new Storage(getApplicationContext()).set("feed.keep.index", Integer.toString(position));
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

        String sel = new Storage(getApplicationContext()).get("refresh.space.index");
        refresh.setSelection(sel == null ? adapter.getCount() - 1 : Integer.parseInt(sel));

        refresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new Storage(getApplicationContext()).set("refresh.space.index", Integer.toString(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private int[] refresh_items = {10 * 60, 30 * 60, 60 * 60, 6 * 60 * 60, 12 * 60 * 60, 24 * 60 * 60};
    private int[] keep_items = {50, 200, 500, 1000};
}