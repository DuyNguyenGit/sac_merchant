package com.sacombank.merchants.injection;

import android.support.annotation.NonNull;

import com.sacombank.merchants.interactor.HistoryInteractor;
import com.sacombank.merchants.interactor.impl.HistoryInteractorImpl;
import com.sacombank.merchants.presenter.loader.PresenterFactory;
import com.sacombank.merchants.presenter.HistoryPresenter;
import com.sacombank.merchants.presenter.impl.HistoryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class HistoryViewModule {
    @Provides
    public HistoryInteractor provideInteractor() {
        return new HistoryInteractorImpl();
    }

    @Provides
    public PresenterFactory<HistoryPresenter> providePresenterFactory(@NonNull final HistoryInteractor interactor) {
        return new PresenterFactory<HistoryPresenter>() {
            @NonNull
            @Override
            public HistoryPresenter create() {
                return new HistoryPresenterImpl(interactor);
            }
        };
    }
}
