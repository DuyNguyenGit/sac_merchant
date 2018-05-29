package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.QrcodeStaticInteractor;
import com.sacombank.merchants.interactor.impl.QrcodeStaticInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.QrcodeStaticPresenter;
import com.sacombank.merchants.presenter.impl.QrcodeStaticPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class QrcodeStaticViewModule {
    @Provides
    public QrcodeStaticInteractor provideInteractor() {
        return new QrcodeStaticInteractorImpl();
    }

    @Provides
    public PresenterFactory<QrcodeStaticPresenter> providePresenterFactory(@NonNull final QrcodeStaticInteractor interactor) {
        return new PresenterFactory<QrcodeStaticPresenter>() {
            @NonNull
            @Override
            public QrcodeStaticPresenter create() {
                return new QrcodeStaticPresenterImpl(interactor);
            }
        };
    }
}
