package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.SignInInteractor;
import com.sacombank.merchants.interactor.impl.SignInInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.SignInPresenter;
import com.sacombank.merchants.presenter.impl.SignInPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class SignInViewModule {
    @Provides
    public SignInInteractor provideInteractor() {
        return new SignInInteractorImpl();
    }

    @Provides
    public PresenterFactory<SignInPresenter> providePresenterFactory(@NonNull final SignInInteractor interactor) {
        return new PresenterFactory<SignInPresenter>() {
            @NonNull
            @Override
            public SignInPresenter create() {
                return new SignInPresenterImpl(interactor);
            }
        };
    }
}
