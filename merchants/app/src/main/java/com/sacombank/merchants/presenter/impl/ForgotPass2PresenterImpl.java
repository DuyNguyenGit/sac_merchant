package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.sacombank.merchants.api.ApiManager;
import com.sacombank.merchants.model.jsonobject.PasswordData;
import com.sacombank.merchants.presenter.ForgotPass2Presenter;
import com.sacombank.merchants.view.ForgotPass2View;
import com.sacombank.merchants.interactor.ForgotPass2Interactor;
import com.stb.api.bo.MerchantPasswordChange;
import com.stb.api.listeners.ApiResponse;

import javax.inject.Inject;

public final class ForgotPass2PresenterImpl extends BasePresenterImpl<ForgotPass2View> implements ForgotPass2Presenter {
    private static final String TAG = ForgotPass2PresenterImpl.class.getSimpleName();
    /**
     * The interactor
     */
    @NonNull
    private final ForgotPass2Interactor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ForgotPass2PresenterImpl(@NonNull ForgotPass2Interactor interactor) {
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
    public void changePassword(PasswordData passwordData) {
        MerchantPasswordChange data = new MerchantPasswordChange();
        data.OldPassword = passwordData.getPassOld();
        data.NewPassword = passwordData.getPassNew();
        ApiManager.requestChangePassword(data, new ApiResponse<MerchantPasswordChange>() {
            @Override
            public void onSuccess(MerchantPasswordChange result) {
                Log.e(TAG, "onSuccess: >>>" + new Gson().toJson(result));
                if (mView != null) {
                    mView.changePassSuccess(result);
                }
            }

            @Override
            public void onError(MerchantPasswordChange error) {
                Log.e(TAG, "onError: >>>" + new Gson().toJson(error));
                if (mView != null) {
                    mView.changePassError(error);
                }
            }
        });
    }
}