package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;

import com.sacombank.merchants.presenter.CreateQrcodePresenter;
import com.sacombank.merchants.view.CreateQrcodeView;
import com.sacombank.merchants.interactor.CreateQrcodeInteractor;

import javax.inject.Inject;

public final class CreateQrcodePresenterImpl extends BasePresenterImpl<CreateQrcodeView> implements CreateQrcodePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final CreateQrcodeInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public CreateQrcodePresenterImpl(@NonNull CreateQrcodeInteractor interactor) {
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