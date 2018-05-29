package com.sacombank.merchants.presenter;

import com.sacombank.merchants.model.jsonobject.QRCodeInputData;
import com.sacombank.merchants.view.QrcodeDynamicInputView;

public interface QrcodeDynamicInputPresenter extends BasePresenter<QrcodeDynamicInputView> {
    void generateQRCode(QRCodeInputData data);
}