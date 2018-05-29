package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.AccountLanguageInteractor;
import com.sacombank.merchants.interactor.impl.AccountLanguageInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.AccountLanguagePresenter;
import com.sacombank.merchants.presenter.impl.AccountLanguagePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AccountLanguageViewModule {
    @Provides
    public AccountLanguageInteractor provideInteractor() {
        return new AccountLanguageInteractorImpl();
    }

    @Provides
    public PresenterFactory<AccountLanguagePresenter> providePresenterFactory(@NonNull final AccountLanguageInteractor interactor) {
        return new PresenterFactory<AccountLanguagePresenter>() {
            @NonNull
            @Override
            public AccountLanguagePresenter create() {
                return new AccountLanguagePresenterImpl(interactor);
            }
        };
    }
}
