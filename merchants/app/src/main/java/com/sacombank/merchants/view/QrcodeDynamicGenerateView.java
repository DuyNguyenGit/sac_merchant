package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface QrcodeDynamicGenerateView {
    <T> void sharedSuccess(T result);
    <T> void sharedError(T result);
}