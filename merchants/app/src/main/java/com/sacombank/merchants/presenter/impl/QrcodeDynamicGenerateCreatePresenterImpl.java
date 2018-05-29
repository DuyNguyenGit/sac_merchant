package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;

import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.model.jsonobject.QRCodeSharing;
import com.sacombank.merchants.presenter.QrcodeDynamicGenerateCreatePresenter;
import com.sacombank.merchants.view.QrcodeDynamicGenerateCreateView;
import com.sacombank.merchants.interactor.QrcodeDynamicGenerateCreateInteractor;
import com.stb.api.bo.MerchantQRShare;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class QrcodeDynamicGenerateCreatePresenterImpl extends BasePresenterImpl<QrcodeDynamicGenerateCreateView> implements QrcodeDynamicGenerateCreatePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final QrcodeDynamicGenerateCreateInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public QrcodeDynamicGenerateCreatePresenterImpl(@NonNull QrcodeDynamicGenerateCreateInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }

    @Override
    public void shareQRCode(QRCodeSharing sharingData) {
        MerchantQRShare data = new MerchantQRShare();
        data.ToEmail = sharingData.getEmail();
        data.QRStream = sharingData.getQrCodeStream();
        data.Note = sharingData.getNote();
        ApiManager.requestQRCodeShare(data, new ApiResponse<MerchantQRShare>() {
            @Override
            public void onSuccess(MerchantQRShare result) {
                if (mView != null) {
                    mView.sharedSuccess(result);
                }
            }

            @Override
            public void onError(MerchantQRShare error) {
                if (mView != null) {
                    mView.sharedError(error);
                }
            }
        });
    }
}