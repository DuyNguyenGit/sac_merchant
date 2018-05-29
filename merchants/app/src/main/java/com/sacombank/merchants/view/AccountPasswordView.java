package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface AccountPasswordView {
    <T> void changePassSuccess(T result);
    <T> void changePassError(T result);
}