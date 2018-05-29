package com.sacombank.merchants.presenter;

import com.sacombank.merchants.model.jsonobject.LoginData;
import com.sacombank.merchants.view.SignInView;

public interface SignInPresenter extends BasePresenter<SignInView> {

    void login(LoginData loginData);
}