package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.NfcInteractor;
import com.sacombank.merchants.interactor.impl.NfcInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.NfcPresenter;
import com.sacombank.merchants.presenter.impl.NfcPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class NfcViewModule {
    @Provides
    public NfcInteractor provideInteractor() {
        return new NfcInteractorImpl();
    }

    @Provides
    public PresenterFactory<NfcPresenter> providePresenterFactory(@NonNull final NfcInteractor interactor) {
        return new PresenterFactory<NfcPresenter>() {
            @NonNull
            @Override
            public NfcPresenter create() {
                return new NfcPresenterImpl(interactor);
            }
        };
    }
}
