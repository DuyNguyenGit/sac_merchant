package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.QrcodeDynamicGenerateInteractor;
import com.sacombank.merchants.interactor.impl.QrcodeDynamicGenerateInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.QrcodeDynamicGeneratePresenter;
import com.sacombank.merchants.presenter.impl.QrcodeDynamicGeneratePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class QrcodeDynamicGenerateViewModule {
    @Provides
    public QrcodeDynamicGenerateInteractor provideInteractor() {
        return new QrcodeDynamicGenerateInteractorImpl();
    }

    @Provides
    public PresenterFactory<QrcodeDynamicGeneratePresenter> providePresenterFactory(@NonNull final QrcodeDynamicGenerateInteractor interactor) {
        return new PresenterFactory<QrcodeDynamicGeneratePresenter>() {
            @NonNull
            @Override
            public QrcodeDynamicGeneratePresenter create() {
                return new QrcodeDynamicGeneratePresenterImpl(interactor);
            }
        };
    }
}
