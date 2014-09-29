package com.odong.rssreader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 14-9-28.
 */
public class ItemListAdapter extends BaseAdapter {
    public ItemListAdapter(Context context, List<Map<String, String>> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String titleS = items.get(position).get("title");
        String summaryS = items.get(position).get("summary");

        View tmp = convertView == null ? (LinearLayout) View.inflate(context, R.layout.item_list_item, null) : convertView;

        TextView titleV = (TextView) tmp.findViewById(R.id.item_list_item_title);
        titleV.setText(titleS);

        WebView summaryV = (WebView) tmp.findViewById(R.id.item_list_item_summary);
        summaryV.getSettings().setDefaultTextEncodingName("utf-8");
        summaryV.loadData(summaryS, "text/html", null);


        return tmp;

    }

    private Context context;
    private List<Map<String, String>> items;
}
