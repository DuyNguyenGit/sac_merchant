package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface HistoryDetailView {
    <T> void cancelTransactionSuccess(T result);
    <T> void cancelTransactionError(T result);
}