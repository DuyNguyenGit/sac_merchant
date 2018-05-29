package com.sacombank.merchants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sacombank.merchants.R;
import com.sacombank.merchants.model.staticmodel.TabModel;
import com.sacombank.merchants.widgets.TabCustom;

import java.util.List;

/**
 * Created by DUY on 4/6/2017.
 */

public class TabAdapter extends BaseAdapter {

    private Context context;
    private List<TabModel> tabModelList;
    private OnTabClickListener listener;
    private int activeTabId = -1;

    public TabAdapter(Context context, List<TabModel> tabModelList, OnTabClickListener listener) {
        this.context = context;
        this.tabModelList = tabModelList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return tabModelList.size();
    }

    @Override
    public TabModel getItem(int i) {
        return tabModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TabViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tab_item_layout, viewGroup, false);

            holder = new TabViewHolder();
            holder.tabView = (TabCustom) view.findViewById(R.id.tab_view);
            view.setTag(holder);
        } else {
            holder = (TabViewHolder) view.getTag();
        }

        final TabModel tab = getItem(position);
        holder.tabView.updateUI(tab, tab.getId() == activeTabId);
        holder.tabView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null && tab.getId() != activeTabId) {
                    listener.onClick(tab);
                }
            }
        });
        return view;
    }

    public void setActiveTabId(int id) {
        activeTabId = id;
    }

    private static class TabViewHolder {
        TabCustom tabView;
    }

    public interface OnTabClickListener {
        void onClick(TabModel tab);
    }
}
