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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.odong.rssreader.store.Storage;
import com.odong.rssreader.utils.Rss;

import java.util.*;

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
                refreshFeedList();
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_add_feed:
                startActivity(new Intent(this, FeedAddActivity.class));
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

        Storage.setContext(this);
        initFeedList();

        syncJob();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dlg_exit_message)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        syncTimer.cancel();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.btn_no, null)
                .show();
    }

    private void onMessage(int title, int body, int icon) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("icon", icon);
        startActivity(intent);
    }


    private void initFeedList() {

        ListView lvFeeds = (ListView) findViewById(R.id.lv_feeds);
        lvFeedItems = new ArrayList<Map<String, String>>();
        lvFeedIds = new ArrayList<Integer>();


        lvFeedAdapter = new SimpleAdapter(this, lvFeedItems, R.layout.feed_list_item, new String[]{"title", "summary"}, new int[]{R.id.feed_list_item_title, R.id.feed_list_item_summary});
        lvFeeds.setAdapter(lvFeedAdapter);

        lvFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FeedShowActivity.class);
                intent.putExtra("id", lvFeedIds.get(position));
                intent.putExtra("title", lvFeedItems.get(position).get("title"));
                startActivity(intent);
            }
        });

        lvFeeds.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.dlg_delete_title)
                        .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Storage s = Storage.getInstance();
                                s.delFeed(lvFeedIds.get(position));
                                refreshFeedList();
                            }
                        })
                        .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });

        refreshFeedList();
    }

    private void refreshFeedList() {
        lvFeedItems.clear();
        lvFeedIds.clear();

        final Storage storage = Storage.getInstance();

        storage.listFeed(new Storage.FeedCallback() {
            @Override
            public void run(int id, String url, String title, String description, Date lastSync) {
                lvFeedIds.add(id);

                Map<String, String> v = new HashMap<String, String>();

                v.put("title", title + "(" + storage.countUnreadItem(id) + ")");
                v.put("summary", description);
                lvFeedItems.add(v);
            }
        });

        lvFeedAdapter.notifyDataSetChanged();
    }

    private void syncJob() {
        final Storage s = Storage.getInstance();
        String space = s.get("refresh.space.index");

        syncTimer = new Timer();
        syncTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                s.listFeed(new Storage.FeedCallback() {
                    @Override
                    public void run(int id, String url, String title, String description, Date lastSync) {
                        try {
                            Rss rss = new Rss(url);
                            s.addItems(id, rss.getItemList());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 500, (space == null ? 24 * 60 * 60 : Integer.parseInt(space)) * 1000);

    }

    private List<Map<String, String>> lvFeedItems;
    private SimpleAdapter lvFeedAdapter;
    private List<Integer> lvFeedIds;
    private Timer syncTimer;
}
