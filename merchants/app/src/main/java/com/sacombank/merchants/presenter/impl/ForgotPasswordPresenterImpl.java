package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;

import com.sacombank.merchants.presenter.ForgotPasswordPresenter;
import com.sacombank.merchants.view.ForgotPasswordView;
import com.sacombank.merchants.interactor.ForgotPasswordInteractor;

import javax.inject.Inject;

public final class ForgotPasswordPresenterImpl extends BasePresenterImpl<ForgotPasswordView> implements ForgotPasswordPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final ForgotPasswordInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ForgotPasswordPresenterImpl(@NonNull ForgotPasswordInteractor interactor) {
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
}