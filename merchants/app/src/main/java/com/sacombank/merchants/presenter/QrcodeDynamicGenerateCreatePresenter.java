package com.sacombank.merchants.presenter;

import com.sacombank.merchants.model.jsonobject.QRCodeSharing;
import com.sacombank.merchants.view.QrcodeDynamicGenerateCreateView;

public interface QrcodeDynamicGenerateCreatePresenter extends BasePresenter<QrcodeDynamicGenerateCreateView> {
    void shareQRCode(QRCodeSharing note);
}