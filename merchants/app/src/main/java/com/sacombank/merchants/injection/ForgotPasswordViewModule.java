package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.ForgotPasswordInteractor;
import com.sacombank.merchants.interactor.impl.ForgotPasswordInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.ForgotPasswordPresenter;
import com.sacombank.merchants.presenter.impl.ForgotPasswordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ForgotPasswordViewModule {
    @Provides
    public ForgotPasswordInteractor provideInteractor() {
        return new ForgotPasswordInteractorImpl();
    }

    @Provides
    public PresenterFactory<ForgotPasswordPresenter> providePresenterFactory(@NonNull final ForgotPasswordInteractor interactor) {
        return new PresenterFactory<ForgotPasswordPresenter>() {
            @NonNull
            @Override
            public ForgotPasswordPresenter create() {
                return new ForgotPasswordPresenterImpl(interactor);
            }
        };
    }
}
