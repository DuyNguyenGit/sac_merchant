package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface ForgotPass2View {

    <T> void changePassSuccess(T result);
    <T> void changePassError(T result);
}