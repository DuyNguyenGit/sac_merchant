package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.interactor.AccountInfoInteractor;
import com.sacombank.merchants.presenter.AccountInfoPresenter;
import com.sacombank.merchants.view.AccountInfoView;
import com.stb.api.bo.MerchantAccountInquiry;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class AccountInfoPresenterImpl extends BasePresenterImpl<AccountInfoView> implements AccountInfoPresenter {
    private static final String TAG = AccountInfoPresenterImpl.class.getSimpleName();
    /**
     * The interactor
     */
    @NonNull
    private final AccountInfoInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public AccountInfoPresenterImpl(@NonNull AccountInfoInteractor interactor) {
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
    public void getAccountInfo() {
        ApiManager.requestManageAccount(new ApiResponse<MerchantAccountInquiry>() {

            @Override
            public void onSuccess(MerchantAccountInquiry result) {
                Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(result));
                if (mView != null) {
                    mView.getAccountSuccess(result);
                }
            }

            @Override
            public void onError(MerchantAccountInquiry error) {
                Log.e(TAG, "onError: >>>" + new Gson().toJson(error));
                if (mView != null) {
                    mView.getAccountError(error);
                }
            }
        });
    }
}