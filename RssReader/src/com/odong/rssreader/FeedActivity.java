package com.odong.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
                    refreshItemList();
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
                    Storage s = Storage.getInstance();
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


        lvItemAdapter = new SimpleAdapter(this, lvItems, R.layout.item_list_item, new String[]{"title", "summary"}, new int[]{R.id.item_list_item_title, R.id.item_list_item_summary});
//        lvItemAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, String textRepresentation) {
//                if (view instanceof WebView) {
//                    ((WebView) view).loadData(textRepresentation, "text/html", null);
//                    return true;
//                }
//                return false;
//            }
//        });
        lv.setAdapter(lvItemAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Storage.getInstance().setItemRead(lvIds.get(position));
                openItem(position);
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        loadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        callback = new Storage.ItemCallback() {
            @Override
            public void run(int id, String link, String title, String description, String pubDate, boolean read) {
                lvIds.add(id);
                lvLinks.add(link);

                Map<String, String> v = new HashMap<String, String>();
                v.put("title", read ? title : "[" + getString(R.string.lbl_unread) + "]" + title);
                v.put("summary", Html.fromHtml(description).toString());
                lvItems.add(v);
            }
        };

        refreshItemList();
    }

    private void openItem(int position) {
        Intent intent = new Intent(this, ItemActivity.class);

        String title = lvItems.get(position).get("title");
        String unread = getString(R.string.lbl_unread);
        if (title.indexOf(unread) == 1) {
            title = title.substring(unread.length() + 2);
        }

        intent.putExtra("title", title);
        intent.putExtra("link", lvLinks.get(position));
        startActivity(intent);
    }

    private void refreshItemList() {
        lvItems.clear();
        lvLinks.clear();
        lvIds.clear();

        Storage storage = Storage.getInstance();
        storage.listItem(feedId, 0, Constants.ITEM_PAGE, callback);
        lvItemAdapter.notifyDataSetChanged();
    }

    private void loadMore() {
        int offset = lvIds.get(lvIds.size() - 1);
        Log.d("RSS READER", "加载" + offset);
        Storage storage = Storage.getInstance();
        storage.listItem(feedId, offset, Constants.ITEM_PAGE, callback);
        lvItemAdapter.notifyDataSetChanged();
    }

    private int feedId;

    private List<Map<String, String>> lvItems;
    private List<String> lvLinks;
    private List<Integer> lvIds;
    private SimpleAdapter lvItemAdapter;
    private Storage.ItemCallback callback;
}