package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.ForgotPass2Interactor;
import com.sacombank.merchants.interactor.impl.ForgotPass2InteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.ForgotPass2Presenter;
import com.sacombank.merchants.presenter.impl.ForgotPass2PresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ForgotPass2ViewModule {
    @Provides
    public ForgotPass2Interactor provideInteractor() {
        return new ForgotPass2InteractorImpl();
    }

    @Provides
    public PresenterFactory<ForgotPass2Presenter> providePresenterFactory(@NonNull final ForgotPass2Interactor interactor) {
        return new PresenterFactory<ForgotPass2Presenter>() {
            @NonNull
            @Override
            public ForgotPass2Presenter create() {
                return new ForgotPass2PresenterImpl(interactor);
            }
        };
    }
}
