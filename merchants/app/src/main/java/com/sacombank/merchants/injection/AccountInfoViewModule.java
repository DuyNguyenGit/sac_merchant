package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.AccountInfoInteractor;
import com.sacombank.merchants.interactor.impl.AccountInfoInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.AccountInfoPresenter;
import com.sacombank.merchants.presenter.impl.AccountInfoPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AccountInfoViewModule {
    @Provides
    public AccountInfoInteractor provideInteractor() {
        return new AccountInfoInteractorImpl();
    }

    @Provides
    public PresenterFactory<AccountInfoPresenter> providePresenterFactory(@NonNull final AccountInfoInteractor interactor) {
        return new PresenterFactory<AccountInfoPresenter>() {
            @NonNull
            @Override
            public AccountInfoPresenter create() {
                return new AccountInfoPresenterImpl(interactor);
            }
        };
    }
}
