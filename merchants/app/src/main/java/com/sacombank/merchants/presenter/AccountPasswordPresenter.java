package com.sacombank.merchants.presenter;

import com.sacombank.merchants.model.jsonobject.PasswordData;
import com.sacombank.merchants.view.AccountPasswordView;

public interface AccountPasswordPresenter extends BasePresenter<AccountPasswordView> {

    void changePassword(PasswordData passwordData);
}