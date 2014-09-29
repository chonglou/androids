package com.odong.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.odong.rssreader.store.Storage;
import com.odong.rssreader.utils.Rss;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 14-9-22.
 */
public class FeedActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.feed_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                onRefresh();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        Intent intent = getIntent();
        feedId = intent.getIntExtra("id", 1);
        setTitle(intent.getStringExtra("title"));

        initItemList();
    }

    private void onRefresh() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                String msg = (String) message.obj;
                if (msg.equals(Constants.SUCCESS)) {
                    refreshItemList(0);
                } else {
                    Constants.alert(FeedActivity.this, msg);
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                try {
                    Storage s = new Storage(FeedActivity.this);
                    Rss rss = new Rss(s.getFeedUrl(feedId));
                    s.addItems(feedId, rss.getItemList());
                    msg.obj = Constants.SUCCESS;
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    msg.obj = getString(R.string.exception_xml_parser);
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.obj = getString(R.string.exception_network);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.obj = e.getMessage();
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void initItemList() {

        ListView lv = (ListView) findViewById(R.id.lv_items);
        lvItems = new ArrayList<Map<String, String>>();
        lvLinks = new ArrayList<String>();
        lvIds = new ArrayList<Integer>();


        lvItemAdapter = new SimpleAdapter(this, lvItems, R.layout.list_item, new String[]{"title", "summary"}, new int[]{R.id.item_title, R.id.item_summary});
        lv.setAdapter(lvItemAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo
            }
        });

        refreshItemList(0);
    }

    private void refreshItemList(int offset) {
        lvItems.clear();
        lvLinks.clear();
        lvIds.clear();

        Storage storage = new Storage(this);

        storage.listItem(feedId, offset, Constants.ITEM_PAGE, new Storage.ItemCallback() {
            @Override
            public void run(int id, String link, String title, String description, String pubDate) {
                lvIds.add(id);
                lvLinks.add(link);

                Map<String, String> v = new HashMap<String, String>();
                v.put("title", title);
                v.put("summary", description);
                lvItems.add(v);
            }
        });

        lvItemAdapter.notifyDataSetChanged();
    }

    private int feedId;

    private List<Map<String, String>> lvItems;
    private List<String> lvLinks;
    private List<Integer> lvIds;
    private SimpleAdapter lvItemAdapter;
}