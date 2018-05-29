package com.sacombank.merchants.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sacombank.merchants.presenter.BasePresenter;

/**
 * Abstract presenter implementation that contains base implementation for other presenters.
 * Subclasses must call super for all {@link BasePresenter} method overriding.
 */
public abstract class BasePresenterImpl<V> implements BasePresenter<V> {
    private static final String TAG = BasePresenterImpl.class.getSimpleName();
    /**
     * The view
     */
    @Nullable
    protected V mView;

    @Override
    public void onViewAttached(@NonNull V view) {
        mView = view;
    }


    @Override
    public void onStart(boolean viewCreated) {
        Log.e(TAG, "onStart: >>>" + mView);
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop: >>>" + mView);
    }


    @Override
    public void onViewDetached() {
        Log.e(TAG, "onViewDetached: >>>" + mView);
        mView = null;
    }

    @Override
    public void onPresenterDestroyed() {
        Log.e(TAG, "onPresenterDestroyed: >>>" + mView);
    }

}
