package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.AccountPasswordInteractor;
import com.sacombank.merchants.interactor.impl.AccountPasswordInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.AccountPasswordPresenter;
import com.sacombank.merchants.presenter.impl.AccountPasswordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AccountPasswordViewModule {
    @Provides
    public AccountPasswordInteractor provideInteractor() {
        return new AccountPasswordInteractorImpl();
    }

    @Provides
    public PresenterFactory<AccountPasswordPresenter> providePresenterFactory(@NonNull final AccountPasswordInteractor interactor) {
        return new PresenterFactory<AccountPasswordPresenter>() {
            @NonNull
            @Override
            public AccountPasswordPresenter create() {
                return new AccountPasswordPresenterImpl(interactor);
            }
        };
    }
}
