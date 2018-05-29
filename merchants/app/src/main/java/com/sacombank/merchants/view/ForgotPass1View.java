package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface ForgotPass1View {
    <T> void forgotPassSuccess(T result);
    <T> void forgotPassError(T result);
}