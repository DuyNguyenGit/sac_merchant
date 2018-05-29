package com.sacombank.merchants.presenter;

import com.sacombank.merchants.view.AccountInfoView;

public interface AccountInfoPresenter extends BasePresenter<AccountInfoView> {
    void getAccountInfo();
}