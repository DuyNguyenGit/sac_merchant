package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.interactor.HistoryDetailInteractor;
import com.sacombank.merchants.presenter.HistoryDetailPresenter;
import com.sacombank.merchants.view.HistoryDetailView;
import com.stb.api.bo.MerchantTransactionReversal;
import com.stb.api.bo.TransactionObject;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class HistoryDetailPresenterImpl extends BasePresenterImpl<HistoryDetailView> implements HistoryDetailPresenter {
    private static final String TAG = HistoryDetailPresenterImpl.class.getSimpleName();
    /**
     * The interactor
     */
    @NonNull
    private final HistoryDetailInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public HistoryDetailPresenterImpl(@NonNull HistoryDetailInteractor interactor) {
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
    public void cancelTransaction(TransactionObject transactionObject, String password) {
        MerchantTransactionReversal data = new MerchantTransactionReversal();
        data.ReferenceNo = transactionObject.ReferenceNo;//optional
        data.BatchNo = transactionObject.BatchNo;//optional
        data.ApprovalCode = transactionObject.ApprovalCode;//optional
        data.Amount = transactionObject.Amount;
        data.MaskCardNumber = transactionObject.MaskCardNumber;
        data.CardCode = transactionObject.CardCode;
        data.AccountPassword = password;
        ApiManager.requestTransactionReversal(data, new ApiResponse<MerchantTransactionReversal>() {

            @Override
            public void onSuccess(MerchantTransactionReversal result) {
                Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(result));
                if (mView != null) {
                    mView.cancelTransactionSuccess(result);
                }
            }

            @Override
            public void onError(MerchantTransactionReversal error) {
                Log.e(TAG, "onError: >>>" + new Gson().toJson(error));
                if (mView != null) {
                    mView.cancelTransactionError(error);
                }
            }
        });
    }
}