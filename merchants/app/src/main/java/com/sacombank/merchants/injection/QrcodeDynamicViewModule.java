package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.QrcodeDynamicInteractor;
import com.sacombank.merchants.interactor.impl.QrcodeDynamicInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.QrcodeDynamicPresenter;
import com.sacombank.merchants.presenter.impl.QrcodeDynamicPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class QrcodeDynamicViewModule {
    @Provides
    public QrcodeDynamicInteractor provideInteractor() {
        return new QrcodeDynamicInteractorImpl();
    }

    @Provides
    public PresenterFactory<QrcodeDynamicPresenter> providePresenterFactory(@NonNull final QrcodeDynamicInteractor interactor) {
        return new PresenterFactory<QrcodeDynamicPresenter>() {
            @NonNull
            @Override
            public QrcodeDynamicPresenter create() {
                return new QrcodeDynamicPresenterImpl(interactor);
            }
        };
    }
}
