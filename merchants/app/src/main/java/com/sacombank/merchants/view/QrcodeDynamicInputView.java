package com.sacombank.merchants.view;

import android.support.annotation.UiThread;

@UiThread
public interface QrcodeDynamicInputView {
    <T> void generateQRCodeSuccess(T result);
    <T> void generateQRCodeError(T result);
}