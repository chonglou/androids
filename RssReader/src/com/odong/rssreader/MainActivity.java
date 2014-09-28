package com.odong.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.odong.rssreader.store.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            case R.id.action_refresh:
                onRefresh();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_add_feed:
                onAddFeed();
                break;
            case R.id.action_help:
                onMessage(R.string.help_title, R.string.help_body);
                break;
            case R.id.action_about:
                onMessage(R.string.about_title, R.string.about_body);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Storage storage = new Storage(getApplicationContext());
        storage.log("启动");

        lvFeeds = (ListView) findViewById(R.id.lv_feeds);

        List<HashMap<String, String>> feeds = new ArrayList<HashMap<String, String>>();
        //todo FEED列表
        for (int i = 1; i < 10; i++) {
            HashMap<String, String> v = new HashMap<String, String>();
            v.put("title", "Title" + i);
            v.put("summary", "Summary" + i);
            feeds.add(v);
        }

        SimpleAdapter sa = new SimpleAdapter(this, feeds, R.layout.feed, new String[]{"title", "summary"}, new int[]{R.id.feed_title, R.id.feed_summary});

        lvFeeds.setAdapter(sa);
        //lv.setClickable(true);
        lvFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lvFeeds.getItemAtPosition(position);
                //todo
            }
        });
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

    private void onMessage(int title, int body) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        startActivity(intent);
    }

    private void onRefresh() {

    }

    private void onAddFeed() {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setMessage(R.string.dlg_addFeed_title)
                .setView(input)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo
                        String value = input.getText().toString();
                    }
                })

                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo
                    }
                })
                .show();
    }

    private ListView lvFeeds;
}
