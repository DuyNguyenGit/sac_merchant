package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;

import com.sacombank.merchants.presenter.HomePagePresenter;
import com.sacombank.merchants.view.HomePageView;
import com.sacombank.merchants.interactor.HomePageInteractor;

import javax.inject.Inject;

public final class HomePagePresenterImpl extends BasePresenterImpl<HomePageView> implements HomePagePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final HomePageInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public HomePagePresenterImpl(@NonNull HomePageInteractor interactor) {
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