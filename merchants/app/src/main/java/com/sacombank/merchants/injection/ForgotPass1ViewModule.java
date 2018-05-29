package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.ForgotPass1Interactor;
import com.sacombank.merchants.interactor.impl.ForgotPass1InteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.ForgotPass1Presenter;
import com.sacombank.merchants.presenter.impl.ForgotPass1PresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ForgotPass1ViewModule {
    @Provides
    public ForgotPass1Interactor provideInteractor() {
        return new ForgotPass1InteractorImpl();
    }

    @Provides
    public PresenterFactory<ForgotPass1Presenter> providePresenterFactory(@NonNull final ForgotPass1Interactor interactor) {
        return new PresenterFactory<ForgotPass1Presenter>() {
            @NonNull
            @Override
            public ForgotPass1Presenter create() {
                return new ForgotPass1PresenterImpl(interactor);
            }
        };
    }
}
