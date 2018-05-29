package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface MainView {
    public void addToolBar();
    public void addTabBar();
    public void showMessage(String msg);

    public void showToast(String msg);
}