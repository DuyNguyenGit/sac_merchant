package com.sacombank.merchants.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.sacombank.merchants.R;
import com.sacombank.merchants.adapter.TabAdapter;
import com.sacombank.merchants.model.staticmodel.TabModel;
import com.sacombank.merchants.util.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DUY on 7/23/2017.
 */

public class FooterMenu extends LinearLayout {

    private View mRootView;
    private GridView gridView;
    private TabAdapter adapter;
    private List<TabModel> tabModelList = new ArrayList<>();

    public FooterMenu(Context context) {
        super(context);

        initializeViews(context);
    }

    public FooterMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        initializeViews(context);
    }

    public FooterMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initializeViews(context);
    }

    void initializeViews(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = layoutInflater.inflate(R.layout.tabbar_footer, null);
        addView(mRootView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        gridView = (GridView) mRootView.findViewById(R.id.lv_tab);
        updateUI();
    }

    private void updateUI() {
        TabModel tabModel1 = new TabModel(Constant.FOOTER_CREATE_QR, getResources().getString(R.string.tab_1_title), R.drawable.ic_tab_qrcode, R.drawable.ic_tab_qrcode);
        TabModel tabModel2 = new TabModel(Constant.FOOTER_MOBILEPOS, getResources().getString(R.string.tab_2_title), R.drawable.ic_tab_mobilepos, R.drawable.ic_tab_mobilepos);
        TabModel tabModel3 = new TabModel(Constant.FOOTER_HISTORY, getResources().getString(R.string.tab_3_title), R.drawable.ic_tab_history, R.drawable.ic_tab_history);
        TabModel tabModel4 = new TabModel(Constant.FOOTER_INTRODUCE, getResources().getString(R.string.tab_4_title), R.drawable.ic_tab_introduce, R.drawable.ic_tab_introduce);
        tabModelList.add(tabModel1);
        tabModelList.add(tabModel2);
        tabModelList.add(tabModel3);
        tabModelList.add(tabModel4);
    }

    public void setOnTabClickListener(TabAdapter.OnTabClickListener listener) {
        adapter = new TabAdapter(getContext(), tabModelList, listener);
        gridView.setAdapter(adapter);
    }

    public void activeTab(int id) {
        adapter.setActiveTabId(id);
        adapter.notifyDataSetChanged();
    }
}
