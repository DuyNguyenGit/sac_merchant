package com.sacombank.merchants.presenter;

import com.sacombank.merchants.view.HistoryDetailView;
import com.stb.api.bo.TransactionObject;

public interface HistoryDetailPresenter extends BasePresenter<HistoryDetailView> {

    void cancelTransaction(TransactionObject data, String password);
}