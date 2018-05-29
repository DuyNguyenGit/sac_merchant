package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface QrcodeDynamicGenerateCreateView {
    <T> void sharedSuccess(T result);
    <T> void sharedError(T result);
}