package com.sacombank.merchants.presenter;

import com.sacombank.merchants.model.jsonobject.QRCodeSharing;
import com.sacombank.merchants.view.QrcodeDynamicGenerateView;

public interface QrcodeDynamicGeneratePresenter extends BasePresenter<QrcodeDynamicGenerateView> {
    void shareQRCode(QRCodeSharing note);
}