package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.QrcodeDynamicGenerateCreateInteractor;
import com.sacombank.merchants.interactor.impl.QrcodeDynamicGenerateCreateInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.QrcodeDynamicGenerateCreatePresenter;
import com.sacombank.merchants.presenter.impl.QrcodeDynamicGenerateCreatePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class QrcodeDynamicGenerateCreateViewModule {
    @Provides
    public QrcodeDynamicGenerateCreateInteractor provideInteractor() {
        return new QrcodeDynamicGenerateCreateInteractorImpl();
    }

    @Provides
    public PresenterFactory<QrcodeDynamicGenerateCreatePresenter> providePresenterFactory(@NonNull final QrcodeDynamicGenerateCreateInteractor interactor) {
        return new PresenterFactory<QrcodeDynamicGenerateCreatePresenter>() {
            @NonNull
            @Override
            public QrcodeDynamicGenerateCreatePresenter create() {
                return new QrcodeDynamicGenerateCreatePresenterImpl(interactor);
            }
        };
    }
}
