package com.sacombank.merchants.presenter;

import com.sacombank.merchants.model.jsonobject.PasswordData;
import com.sacombank.merchants.view.ForgotPass2View;

public interface ForgotPass2Presenter extends BasePresenter<ForgotPass2View> {

    void changePassword(PasswordData passwordData);
}