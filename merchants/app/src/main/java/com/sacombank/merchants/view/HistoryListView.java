package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface HistoryListView {
    <T> void getHistorySuccess(T result);
    <T> void getHistoryError(T result);
}