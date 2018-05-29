package com.sacombank.merchants.presenter;

import com.sacombank.merchants.view.ForgotPass1View;

public interface ForgotPass1Presenter extends BasePresenter<ForgotPass1View> {
    void forgotPass(String account);
}