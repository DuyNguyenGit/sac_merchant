package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.interactor.HistoryListInteractor;
import com.sacombank.merchants.presenter.HistoryListPresenter;
import com.sacombank.merchants.view.HistoryListView;
import com.stb.api.bo.MerchantTransactionInquiry;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class HistoryListPresenterImpl extends BasePresenterImpl<HistoryListView> implements HistoryListPresenter {
    private static final String TAG = HistoryListPresenterImpl.class.getSimpleName();
    /**
     * The interactor
     */
    @NonNull
    private final HistoryListInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public HistoryListPresenterImpl(@NonNull HistoryListInteractor interactor) {
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
    public void getHistoryTransaction(String cardNo, String date2, int page) {

        MerchantTransactionInquiry data = new MerchantTransactionInquiry();
        data.SearchDate = date2;//optional
        data.LastDigitCardNo = cardNo;//optional
        data.SelectIndex = page;//optional
        data.SelectSize = 20;

        ApiManager.requestTransactionHistory(data, new ApiResponse<MerchantTransactionInquiry>() {
            @Override
            public void onSuccess(MerchantTransactionInquiry result) {
                Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(result));
                if (mView != null) {
                    mView.getHistorySuccess(result);
                }
            }

            @Override
            public void onError(MerchantTransactionInquiry error) {
                Log.e(TAG, "onError: >>>" + new Gson().toJson(error));
                if (mView != null) {
                    mView.getHistoryError(error);
                }
            }
        });
    }
}