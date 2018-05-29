package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.AccountManagerInteractor;
import com.sacombank.merchants.interactor.impl.AccountManagerInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.AccountManagerPresenter;
import com.sacombank.merchants.presenter.impl.AccountManagerPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AccountManagerViewModule {
    @Provides
    public AccountManagerInteractor provideInteractor() {
        return new AccountManagerInteractorImpl();
    }

    @Provides
    public PresenterFactory<AccountManagerPresenter> providePresenterFactory(@NonNull final AccountManagerInteractor interactor) {
        return new PresenterFactory<AccountManagerPresenter>() {
            @NonNull
            @Override
            public AccountManagerPresenter create() {
                return new AccountManagerPresenterImpl(interactor);
            }
        };
    }
}
