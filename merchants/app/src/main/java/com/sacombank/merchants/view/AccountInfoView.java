package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface AccountInfoView {
    <T> void getAccountSuccess(T result);
    <T> void getAccountError(T result);
}