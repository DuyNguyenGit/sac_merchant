package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.interactor.QrcodeDynamicInputInteractor;
import com.sacombank.merchants.model.jsonobject.QRCodeInputData;
import com.sacombank.merchants.presenter.QrcodeDynamicInputPresenter;
import com.sacombank.merchants.view.QrcodeDynamicInputView;
import com.stb.api.bo.MerchantQRGeneration;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class QrcodeDynamicInputPresenterImpl extends BasePresenterImpl<QrcodeDynamicInputView> implements QrcodeDynamicInputPresenter {
    private static final String TAG = QrcodeDynamicInputPresenterImpl.class.getSimpleName();
    /**
     * The interactor
     */
    @NonNull
    private final QrcodeDynamicInputInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public QrcodeDynamicInputPresenterImpl(@NonNull QrcodeDynamicInputInteractor interactor) {
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
    public void generateQRCode(QRCodeInputData data) {
        MerchantQRGeneration object = new MerchantQRGeneration();
        object.Amount = Integer.parseInt(data.getAmount().replace(",", ""));
        object.Tips = Integer.parseInt(data.getTip().replace(",", ""));
        object.BillNumber = data.getBill();
        object.Description = data.getDescription();
        ApiManager.requestCreateQRCodeDynamic(object, new ApiResponse<MerchantQRGeneration>() {

            @Override
            public void onSuccess(MerchantQRGeneration result) {
                Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(result));
                if (mView != null) {
                    mView.generateQRCodeSuccess(result);
                }
            }

            @Override
            public void onError(MerchantQRGeneration error) {
                Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(error));
                if (mView != null) {
                    mView.generateQRCodeError(error);
                }
            }
        });
    }
}