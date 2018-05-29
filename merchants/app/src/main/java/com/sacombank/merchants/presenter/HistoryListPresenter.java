package com.sacombank.merchants.presenter;

import com.sacombank.merchants.view.HistoryListView;

public interface HistoryListPresenter extends BasePresenter<HistoryListView> {
    void getHistoryTransaction(String cardNo, String date2, int page);

}