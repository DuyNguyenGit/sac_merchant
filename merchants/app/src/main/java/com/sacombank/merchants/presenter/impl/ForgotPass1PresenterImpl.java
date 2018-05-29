package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;

import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.presenter.ForgotPass1Presenter;
import com.sacombank.merchants.view.ForgotPass1View;
import com.sacombank.merchants.interactor.ForgotPass1Interactor;
import com.stb.api.bo.MerchantPasswordRetrieval;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class ForgotPass1PresenterImpl extends BasePresenterImpl<ForgotPass1View> implements ForgotPass1Presenter {
    /**
     * The interactor
     */
    @NonNull
    private final ForgotPass1Interactor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ForgotPass1PresenterImpl(@NonNull ForgotPass1Interactor interactor) {
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
    public void forgotPass(String account) {
        MerchantPasswordRetrieval data = new MerchantPasswordRetrieval();
        data.UserName = account;
        ApiManager.requestForgotPassword(data, new ApiResponse<MerchantPasswordRetrieval>() {
            @Override
            public void onSuccess(MerchantPasswordRetrieval result) {
                if (mView != null) {
                    mView.forgotPassSuccess(result);
                }
            }

            @Override
            public void onError(MerchantPasswordRetrieval error) {
                if (mView != null) {
                    mView.forgotPassError(error);
                }
            }
        });
    }
}