package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.QrcodeDynamicInputInteractor;
import com.sacombank.merchants.interactor.impl.QrcodeDynamicInputInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.QrcodeDynamicInputPresenter;
import com.sacombank.merchants.presenter.impl.QrcodeDynamicInputPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class QrcodeDynamicInputViewModule {
    @Provides
    public QrcodeDynamicInputInteractor provideInteractor() {
        return new QrcodeDynamicInputInteractorImpl();
    }

    @Provides
    public PresenterFactory<QrcodeDynamicInputPresenter> providePresenterFactory(@NonNull final QrcodeDynamicInputInteractor interactor) {
        return new PresenterFactory<QrcodeDynamicInputPresenter>() {
            @NonNull
            @Override
            public QrcodeDynamicInputPresenter create() {
                return new QrcodeDynamicInputPresenterImpl(interactor);
            }
        };
    }
}
