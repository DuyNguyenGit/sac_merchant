package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface SignInView {
    <T> void loginSuccess(T result);
    <T> void loginError(T result);
}